package com.aiplatform.service.user.impl;

import com.aiplatform.common.BusinessException;
import com.aiplatform.dto.user.UserInfoResponse;
import com.aiplatform.dto.user.UpdateUserInfoRequest;
import com.aiplatform.entity.User;
import com.aiplatform.entity.UserSign;
import com.aiplatform.mapper.UserMapper;
import com.aiplatform.mapper.UserSignMapper;
import com.aiplatform.service.user.UserService;
import com.aiplatform.util.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserSignMapper signMapper;
    private final RedisUtil redisUtil;

    @Override
    public UserInfoResponse getCurrentUser(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) throw new BusinessException(404, "用户不存在");

        // 查今日签到记录
        Long signedCount = signMapper.selectCount(new LambdaQueryWrapper<UserSign>()
                .eq(UserSign::getUserId, userId)
                .eq(UserSign::getSignDate, LocalDate.now()));
        boolean signedToday = signedCount > 0;

        return UserInfoResponse.builder()
                .id(String.valueOf(user.getId()))
                .email(user.getEmail())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .points(user.getPoints())
                .status(user.getStatus())
                .signedToday(signedToday)
                .build();
    }

    @Override
    @Transactional
    public void updateUserInfo(Long userId, UpdateUserInfoRequest req) {
        // 1分钟内只能修改一次
        String lockKey = "user_update:" + userId;
        if (!redisUtil.setIfAbsent(lockKey, "1", 1, TimeUnit.MINUTES)) {
            throw new BusinessException("操作过于频繁，请1分钟后再试");
        }
        User user = new User();
        user.setId(userId);
        if (req.getNickname() != null) user.setNickname(req.getNickname());
        if (req.getAvatar() != null) user.setAvatar(req.getAvatar());
        userMapper.updateById(user);
    }

    @Override
    @Transactional
    public int dailySign(Long userId) {
        // 防刷锁
        String lockKey = "sign_lock:" + userId;
        if (!redisUtil.setIfAbsent(lockKey, "1", 5, TimeUnit.SECONDS)) {
            throw new BusinessException("操作频繁，请稍后重试");
        }

        LocalDate today = LocalDate.now();
        // 检查今日是否已签到 (配合数据库唯一索引)
        Long count = signMapper.selectCount(new LambdaQueryWrapper<UserSign>()
                .eq(UserSign::getUserId, userId)
                .eq(UserSign::getSignDate, today));
        if (count > 0) throw new BusinessException("今日已签到");

        // 计算连续签到天数
        UserSign lastSign = signMapper.selectOne(new LambdaQueryWrapper<UserSign>()
                .eq(UserSign::getUserId, userId)
                .lt(UserSign::getSignDate, today)
                .orderByDesc(UserSign::getSignDate)
                .last("limit 1"));

        int continuousDays = 1;
        if (lastSign != null && lastSign.getSignDate().equals(today.minusDays(1))) {
            continuousDays = lastSign.getContinuousDays() + 1;
        }

        int addPoints = Math.min(5 + (continuousDays - 1) * 3, 35);

        // 插入签到记录
        UserSign sign = new UserSign();
        sign.setUserId(userId);
        sign.setSignDate(today);
        sign.setContinuousDays(continuousDays);
        signMapper.insert(sign);

        // 增加算力（原子更新）
        userMapper.updatePoints(userId, addPoints);

        return addPoints;
    }

    @Override
    public int getPoints(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) throw new BusinessException(404, "用户不存在");
        return user.getPoints();
    }
}