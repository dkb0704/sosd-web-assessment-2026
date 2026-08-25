package com.example.computingpowerrental.service.impl;

import com.example.computingpowerrental.dto.UpdatePasswordRequest;
import com.example.computingpowerrental.dto.UpdateProfileRequest;
import com.example.computingpowerrental.dto.UserInfoResponse;
import com.example.computingpowerrental.entity.User;
import com.example.computingpowerrental.mapper.UserMapper;
import com.example.computingpowerrental.service.UserService;
import com.example.computingpowerrental.util.RequestHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.computingpowerrental.dto.SignInResponse;
import com.example.computingpowerrental.util.RedisUtil;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

/**
 * @author Lark
 * @ date 2026/5/27  19:21
 * @ description 用户模块业务实现类（核心业务逻辑、调用mapper操作数据库、权限与数据校验、返回DTO给controller）
 */
@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisUtil redisUtil;

    //BCrypt密码加密器（用于校验旧密码、加密新密码）
    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();
    //每日签到奖励算力点数
    private static final int DAILY_SIGN_IN_REWARD = 10;
    //Redis 签到 Key 前缀
    private static final String SIGN_IN_KEY_PREFIX = "user:sign:";

    //获取当前登录用户信息（RequestHolder中保存的是当前请求对应的用户ID）
    @Override
    public UserInfoResponse getCurrentUserInfo() {
        // 从 ThreadLocal 中获取当前登录用户 ID
        Long userId = RequestHolder.getUserId();

        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }

        // 查询数据库
        User user = userMapper.findById(userId);

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 转换为安全 DTO 返回
        return UserInfoResponse.fromUser(user);
    }

    //修改当前登录用户资料
    @Override
    @Transactional
    public UserInfoResponse updateProfile(UpdateProfileRequest request) {

        Long userId = RequestHolder.getUserId();

        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }

        User user = userMapper.findById(userId);

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        if (request.getNickname() != null) {
            user.setNickname(request.getNickname());
        }

        if (request.getAvatar() != null) {
            user.setAvatar(request.getAvatar());
        }

        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }

        // 更新数据库
        userMapper.update(user);

        // 返回更新后的用户信息
        return UserInfoResponse.fromUser(user);
    }

    //修改密码（先验证旧密码，新密码要加密）
    @Override
    @Transactional
    public void updatePassword(UpdatePasswordRequest request) {

        Long userId = RequestHolder.getUserId();

        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }

        User user = userMapper.findById(userId);

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        //BCrypt校验
        boolean matches = passwordEncoder.matches(
                request.getOldPassword(),
                user.getPassword()
        );

        if (!matches) {
            throw new RuntimeException("原密码错误");
        }

        //新密码BCrypt加密
        String encodedPassword = passwordEncoder.encode(
                request.getNewPassword()
        );

        user.setPassword(encodedPassword);

        // 更新数据库
        userMapper.update(user);
    }

    //获取当前用户算力点余额
    @Override
    public Integer getCurrentUserPoints() {

        Long userId = RequestHolder.getUserId();

        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }

        User user = userMapper.findById(userId);

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        return user.getComputePoints();
    }

    //当前用户每日签到领取算力点
    @Override
    @Transactional
    public SignInResponse signIn() {
        //获取当前登录用户 ID
        Long userId = RequestHolder.getUserId();

        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }

        //确认用户真实存在
        User user = userMapper.findById(userId);

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        //检查当前账号是否处于正常启用状态
        if (user.getStatus() == null || user.getStatus() != 1) {
            throw new RuntimeException("当前账号已被禁用");
        }

        //生成当天签到 Key
        String signInKey = SIGN_IN_KEY_PREFIX
                + userId
                + ":"
                + LocalDate.now();

        //计算 Redis Key 距离当天结束还有多少秒
        long secondsUntilTomorrow = getSecondsUntilTomorrow();

        //使用 SETNX 原子写入签到 Key
        boolean signInSuccess = redisUtil.setIfAbsent(
                signInKey,
                "1",
                secondsUntilTomorrow,
                TimeUnit.SECONDS
        );

        if (!signInSuccess) {
            throw new RuntimeException("今日已签到，请勿重复领取");
        }

        //数据库原子增加算力点
        int affectedRows = userMapper.increaseComputePoints(
                userId,
                DAILY_SIGN_IN_REWARD
        );

        if (affectedRows == 0) {
            //数据库更新失败时，删除 Redis 签到记录。
            redisUtil.delete(signInKey);
            throw new RuntimeException("签到奖励发放失败");
        }

        //重新查询用户，获取最新算力余额
        User updatedUser = userMapper.findById(userId);

        if (updatedUser == null) {
            throw new RuntimeException("获取签到后余额失败");
        }

        return new SignInResponse(
                DAILY_SIGN_IN_REWARD,
                updatedUser.getComputePoints()
        );
    }

    //计算从当前时间到第二天零点的秒数，用于设置签到key的过期时间
    private long getSecondsUntilTomorrow() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime tomorrowStart = LocalDateTime.of(
                LocalDate.now().plusDays(1),
                LocalTime.MIDNIGHT
        );

        return Duration.between(now, tomorrowStart).getSeconds();
    }
}
