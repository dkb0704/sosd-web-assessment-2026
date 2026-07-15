package com.example.computingpowerrental.controller;

import com.example.computingpowerrental.dto.ApiResponse;
import com.example.computingpowerrental.dto.LoginRequest;
import com.example.computingpowerrental.dto.LoginResponse;
import com.example.computingpowerrental.dto.RegisterRequest;
import com.example.computingpowerrental.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Lark
 * @ date 2026/5/25  14:10
 * @ description 认证模块
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    //用户登录
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ApiResponse.success("登录成功", response);
    }

    //用户注册
    @PostMapping("/register")
    public ApiResponse<LoginResponse> register(@Valid @RequestBody RegisterRequest request) {
        LoginResponse response = authService.register(request);
        return ApiResponse.success("注册成功", response);
    }

    //刷新token
    @PostMapping("/refresh")
    public ApiResponse<LoginResponse> refreshToken(@RequestHeader("Authorization") String authHeader) {
        String refreshToken = extractToken(authHeader);;
        if (refreshToken == null || refreshToken.trim().isEmpty()) {
            return ApiResponse.error(401, "未提供 Refresh Token");
        }

        LoginResponse response = authService.refreshToken(refreshToken);
        return ApiResponse.success("刷新成功", response);
    }

    //从Authorization请求头中提取Token
    private String extractToken(String authHeader) {
        if (authHeader == null || authHeader.trim().isEmpty()) {
            return null;
        }

        if (authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return authHeader;
    }

    //用户退出登录
    @PostMapping("/logout")
    public ApiResponse<Void> logout(
            @RequestHeader("Authorization") String authHeader) {

        String accessToken = extractToken(authHeader);

        if (accessToken == null || accessToken.trim().isEmpty()) {
            return ApiResponse.error(401, "未提供 Access Token");
        }

        authService.logout(accessToken);

        return ApiResponse.success("退出登录成功", null);
    }
}
