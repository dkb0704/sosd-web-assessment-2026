package com.example.computingpowerrental.service;

import com.example.computingpowerrental.dto.LoginRequest;
import com.example.computingpowerrental.dto.LoginResponse;
import com.example.computingpowerrental.dto.RegisterRequest;

/**
 * @author Lark
 * @ date 2026/5/24  18:31
 * @ description 认证模块接口
 */
public interface AuthService {
    LoginResponse login(LoginRequest request);
    LoginResponse register(RegisterRequest request);
    LoginResponse refreshToken(String refreshToken);
}
