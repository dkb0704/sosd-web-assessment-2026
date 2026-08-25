package com.aiplatform.service.auth;

import com.aiplatform.dto.auth.LoginOrRegisterRequest;
import com.aiplatform.dto.auth.LoginResponse;

public interface AuthService {
    void sendVerifyCode(String email);
    LoginResponse loginOrRegister(LoginOrRegisterRequest request);
}