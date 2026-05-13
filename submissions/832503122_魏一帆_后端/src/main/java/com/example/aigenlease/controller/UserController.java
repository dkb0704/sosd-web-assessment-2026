package com.example.aigenlease.controller;
import org.springframework.web.bind.annotation.*;

import com.example.aigenlease.common.ApiResponse;
import com.example.aigenlease.service.UserService;
import com.example.aigenlease.dto.user.request.UserLoginRequest;
import com.example.aigenlease.dto.user.request.UserRegisterRequest;
import com.example.aigenlease.dto.user.request.UserUpdateRequest;
import com.example.aigenlease.dto.user.response.CheckInResponse;
import com.example.aigenlease.dto.user.response.CheckInStatusResponse;
import com.example.aigenlease.dto.user.response.PointsLogResponse;
import com.example.aigenlease.dto.user.response.UserInfoResponse;
import com.example.aigenlease.dto.user.response.UserLoginResponse;
import com.example.aigenlease.dto.user.response.UserRegisterResponse;
import com.example.aigenlease.common.PageResponse;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/auth/register")
    public ApiResponse<UserRegisterResponse> register(@Valid @RequestBody UserRegisterRequest req) {
        return userService.register(req);
    }

    @PostMapping("/auth/login")
    public ApiResponse<UserLoginResponse> login(@Valid @RequestBody UserLoginRequest req) {
        return userService.login(req);
    }
    
    @PostMapping("/auth/logout")
    public ApiResponse<Void> logout(@RequestAttribute("userId") Long userId) {
    return userService.logout(userId);
    }

    @GetMapping("/users/me")
    public ApiResponse<UserInfoResponse> getUserInfo(@RequestAttribute("userId") Long userId) {
        return userService.getUserInfo(userId);
    }

    @PutMapping("/users/me")
    public ApiResponse<UserInfoResponse> updateUserInfo(
        @RequestAttribute("userId") Long userId,
        @Valid @RequestBody UserUpdateRequest req
    ) {
        return userService.updateUserInfo(userId, req);
    }

    @PostMapping("/users/check-in")
    public ApiResponse<CheckInResponse> checkIn(@RequestAttribute("userId") Long userId) {
    return userService.checkIn(userId);
    }

    @GetMapping("users/check-in/status")
    public ApiResponse<CheckInStatusResponse> getCheckInStatus(@RequestAttribute("userId") Long userId) {
        return userService.getCheckInStatus(userId);
    }

    @GetMapping("/users/points/logs")
    public ApiResponse<PageResponse<PointsLogResponse>> getPointsLogs(
            @RequestAttribute("userId") Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return userService.getPointsLogs(userId, page, size);
    }
}

