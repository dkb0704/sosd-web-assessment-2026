package com.example.computingpowerrental.service.impl;

import com.example.computingpowerrental.dto.LoginRequest;
import com.example.computingpowerrental.dto.LoginResponse;
import com.example.computingpowerrental.dto.RegisterRequest;
import com.example.computingpowerrental.entity.User;
import com.example.computingpowerrental.mapper.UserMapper;
import com.example.computingpowerrental.service.AuthService;
import com.example.computingpowerrental.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.computingpowerrental.util.RedisUtil;

import java.util.concurrent.TimeUnit;

/**
 * @author Lark
 * @ date 2026/5/24  18:33
 * @ description
 */
@Service
public class AuthServiceImpl implements AuthService {
    private static final int USER_ROLE_NORMAL = 0;
    private static final int USER_STATUS_NORMAL = 1;
    private static final int DEFAULT_COMPUTE_POINTS = 0;
    private static final String TOKEN_BLACKLIST_PREFIX = "auth:blacklist:";   //Redis 中 JWT 黑名单的 Key 前缀

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RedisUtil redisUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userMapper.findByAccount(request.getAccount());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new RuntimeException("账号已被禁用，请联系管理员");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        return buildLoginResponse(user);
    }

    @Override
    @Transactional
    public LoginResponse register(RegisterRequest request) {
        User existingUser = userMapper.findByUsername(request.getUsername());
        if (existingUser != null) {
            throw new RuntimeException("用户名已存在");
        }
        User existingEmail = userMapper.findByEmail(request.getEmail());
        if (existingEmail != null) {
            throw new RuntimeException("邮箱已被注册");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setNickname(request.getNickname());

        user.setRole(USER_ROLE_NORMAL);
        user.setStatus(USER_STATUS_NORMAL);
        user.setComputePoints(DEFAULT_COMPUTE_POINTS);

        userMapper.insert(user);

        return buildLoginResponse(user);
    }

    @Override
    public LoginResponse refreshToken(String refreshToken) {
        //验证refreshToken
        if (refreshToken == null || refreshToken.trim().isEmpty()) {
            throw new RuntimeException("Refresh Token不能为空");
        }

        //解析token
        Claims claims;
        try {
            claims = jwtUtil.parseToken(refreshToken);
        } catch (Exception e) {
            throw new RuntimeException("无效的Refresh Token");
        }

        //验证是否为refresh token
        String tokenType = (String) claims.get("type");
        if (!"refresh".equals(tokenType)) {
            throw new RuntimeException("Token类型错误");
        }

        //获取用户信息
        Long userId = jwtUtil.getUserIdFromToken(refreshToken);
        User user = userMapper.findById(userId);

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new RuntimeException("账号已被禁用，请联系管理员");
        }

        return buildLoginResponse(user);
    }

    /**
     * 用户退出登录
     *
     * 实现思路：
     * JWT 本身是无状态的，后端无法直接“删除”已经签发的 Token。
     * 因此退出登录时，将当前 Access Token 写入 Redis 黑名单。
     *
     * 后续每次请求经过 AuthInterceptor 时，
     * 都会先检查该 Token 是否存在于 Redis 黑名单中。
     *
     * @param accessToken 当前请求携带的 Access Token
     */
    @Override
    public void logout(String accessToken) {

        //检查 Token 是否为空
        if (accessToken == null || accessToken.trim().isEmpty()) {
            throw new RuntimeException("Access Token 不能为空");
        }

        //检查 Token 是否有效
        if (!jwtUtil.validateToken(accessToken)) {
            throw new RuntimeException("Token 无效或已过期");
        }

        //退出接口只接收 Access Token
        if (!jwtUtil.isAccessToken(accessToken)) {
            throw new RuntimeException("请使用 Access Token 退出登录");
        }

        //获取 Token 剩余有效时间
        long remainingTime = jwtUtil.getRemainingTime(accessToken);

        if (remainingTime <= 0) {
            throw new RuntimeException("Token 已过期");
        }

        //构建 Redis 黑名单 Key
        String blacklistKey = TOKEN_BLACKLIST_PREFIX + accessToken;

        //将 Token 加入 Redis 黑名单
        redisUtil.set(
                blacklistKey,
                "1",
                remainingTime,
                TimeUnit.SECONDS
        );
    }

    private LoginResponse buildLoginResponse(User user) {
        String accessToken = jwtUtil.generateAccessToken(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );

        String refreshToken = jwtUtil.generateRefreshToken(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );

        LoginResponse response = new LoginResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setExpiresIn(jwtUtil.getRemainingTime(accessToken));
        response.setUserInfo(LoginResponse.UserInfo.fromUser(user));

        return response;
    }
}
