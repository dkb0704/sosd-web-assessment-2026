package com.aiplatform.controller.auth;

import com.aiplatform.common.Result;
import com.aiplatform.dto.auth.LoginOrRegisterRequest;
import com.aiplatform.dto.auth.SendCodeRequest;
import com.aiplatform.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/send-code")
    public Result<?> sendCode(@Valid @RequestBody SendCodeRequest request) {
        authService.sendVerifyCode(request.getEmail());
        return Result.success("验证码已发送", null);
    }

    @PostMapping("/login-or-register")
    public Result<?> loginOrRegister(@Valid @RequestBody LoginOrRegisterRequest request) {
        return Result.success(authService.loginOrRegister(request));
    }

    @PostMapping("/logout")
    public Result<?> logout() {
        return Result.success();
    }
}