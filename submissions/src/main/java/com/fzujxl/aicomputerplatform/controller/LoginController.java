package com.fzujxl.aicomputerplatform.controller;

import com.fzujxl.aicomputerplatform.dto.AuthResponse;
import com.fzujxl.aicomputerplatform.dto.LoginRequest;
import com.fzujxl.aicomputerplatform.dto.RegisterRequest;
import com.fzujxl.aicomputerplatform.common.Result;
import com.fzujxl.aicomputerplatform.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



@RestController
@Slf4j
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    Result<AuthResponse>register(@Valid @RequestBody RegisterRequest registerRequest){
        log.info("收到注册请求： username={}, email={}", registerRequest.getUsername(), registerRequest.getEmail());
        AuthResponse response = userService.register(registerRequest);
        return Result.success("注册成功", response);
    }

    @PostMapping("/login")
    Result<AuthResponse>login(@Valid @RequestBody LoginRequest loginRequest){
        log.info("收到登录请求： account{}", loginRequest.getAccount());
        AuthResponse Response = userService.login(loginRequest);
        return Result.success("登录成功", Response);
    }

}
