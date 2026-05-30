package com.aiplatform.service.auth.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.aiplatform.common.BusinessException;
import com.aiplatform.dto.auth.LoginResponse;
import com.aiplatform.dto.auth.LoginOrRegisterRequest;
import com.aiplatform.entity.User;
import com.aiplatform.mapper.UserMapper;
import com.aiplatform.service.auth.AuthService;
import com.aiplatform.util.JwtUtil;
import com.aiplatform.util.MailUtil;
import com.aiplatform.util.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final MailUtil mailUtil;

    @Override
    public void sendVerifyCode(String email) {
        String code = String.valueOf((int)((Math.random() * 900000) + 100000));
        redisUtil.set("email_code:" + email, code, 5, TimeUnit.MINUTES);
        mailUtil.sendSimpleMail(email, "验证码", "您的验证码是：" + code + "，5分钟内有效。");
    }

    @Transactional
    @Override
    public LoginResponse loginOrRegister(LoginOrRegisterRequest req) {
        String email = req.getEmail();

        // 1. 验证码登录先校验验证码
        if ("code".equals(req.getType())) {
            String storedCode = redisUtil.getString("email_code:" + email);
            if (storedCode == null || !storedCode.equals(req.getCode())) {
                throw new BusinessException("验证码错误或已过期");
            }
            redisUtil.delete("email_code:" + email);
        }

        // 2. 查询用户是否存在
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, email));

        if (user == null) {
            // 新用户注册（密码必填，生成昵称）
            if (req.getPassword() == null || req.getPassword().isEmpty()) {
                throw new BusinessException("密码不能为空");
            }
            user = new User();
            user.setEmail(email);
            user.setPassword(DigestUtil.md5Hex(req.getPassword()));
            user.setNickname("用户" + System.currentTimeMillis() % 10000);
            user.setPoints(0);
            user.setStatus(0);
            user.setRole("user");
            userMapper.insert(user);
        } else {
            // 已存在用户，密码登录校验密码
            if ("password".equals(req.getType())) {
                String md5Pass = DigestUtil.md5Hex(req.getPassword());
                if (!user.getPassword().equals(md5Pass)) {
                    throw new BusinessException("密码错误");
                }
            }
            // 检查账号禁用
            if (user.getStatus() == 1) {
                throw new BusinessException("账号已被禁用");
            }
        }

        // 3. 生成JWT
        String token = jwtUtil.createToken(user.getId(), user.getRole());
        return LoginResponse.builder()
                .token(token)
                .userId(String.valueOf(user.getId()))
                .nickname(user.getNickname())
                .role(user.getRole())
                .build();
    }
}