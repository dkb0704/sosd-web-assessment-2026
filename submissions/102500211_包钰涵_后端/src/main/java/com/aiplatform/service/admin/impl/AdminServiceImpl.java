package com.aiplatform.service.admin.impl;

import com.aiplatform.common.BusinessException;
import com.aiplatform.dto.admin.AdminUserUpdateRequest;
import com.aiplatform.dto.admin.PointsModifyRequest;
import com.aiplatform.dto.user.UserInfoResponse;
import com.aiplatform.entity.User;
import com.aiplatform.mapper.UserMapper;
import com.aiplatform.service.admin.AdminService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserMapper userMapper;

    @Override
    public Page<UserInfoResponse> pageUsers(int page, int size, String nickname) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (nickname != null && !nickname.isEmpty()) {
            wrapper.like(User::getNickname, nickname);
        }
        Page<User> userPage = userMapper.selectPage(new Page<>(page, size), wrapper);
        Page<UserInfoResponse> result = new Page<>(page, size, userPage.getTotal());
        result.setRecords(userPage.getRecords().stream().map(u -> UserInfoResponse.builder()
                .id(String.valueOf(u.getId())).email(u.getEmail()).nickname(u.getNickname())
                .avatar(u.getAvatar()).points(u.getPoints()).status(u.getStatus()).role(u.getRole())
                .build()).toList());
        return result;
    }

    @Override
    public UserInfoResponse getUserDetail(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) throw new BusinessException(404, "用户不存在");
        return UserInfoResponse.builder()
                .id(String.valueOf(user.getId())).email(user.getEmail()).nickname(user.getNickname())
                .avatar(user.getAvatar()).points(user.getPoints()).status(user.getStatus()).role(user.getRole())
                .build();
    }

    @Override
    @Transactional
    public void updateUser(Long userId, AdminUserUpdateRequest req) {
        User user = new User();
        user.setId(userId);
        if (req.getNickname() != null) user.setNickname(req.getNickname());
        if (req.getAvatar() != null) user.setAvatar(req.getAvatar());
        if (req.getStatus() != null) user.setStatus(req.getStatus());
        userMapper.updateById(user);
    }

    @Override
    @Transactional
    public void updateUserStatus(Long userId, Integer status) {
        User user = new User();
        user.setId(userId);
        user.setStatus(status);
        userMapper.updateById(user);
    }

    @Override
    @Transactional
    public void modifyPoints(PointsModifyRequest req) {
        int change = req.getPointsChange();
        // 减扣时需防止超扣（用 updatePoints 自带检查）
        int updated = userMapper.updatePoints(Long.valueOf(req.getUserId()), change);
        if (updated == 0 && change < 0) {
            throw new BusinessException("用户算力不足，扣减失败");
        }
    }
}