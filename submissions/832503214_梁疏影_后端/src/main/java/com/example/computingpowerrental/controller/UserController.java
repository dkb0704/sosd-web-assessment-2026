package com.example.computingpowerrental.controller;

import com.example.computingpowerrental.dto.ApiResponse;
import com.example.computingpowerrental.dto.UpdatePasswordRequest;
import com.example.computingpowerrental.dto.UpdateProfileRequest;
import com.example.computingpowerrental.dto.UserInfoResponse;
import com.example.computingpowerrental.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.computingpowerrental.dto.SignInResponse;

/**
 * @author Lark
 * @ date 2026/5/27  19:38
 * @ description 用户端用户模块
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 获取当前登录用户信息
     * GET /api/user/me
     * Authorization: Bearer accessToken
     */
    @GetMapping("/me")
    public ApiResponse<UserInfoResponse> getCurrentUserInfo() {
        UserInfoResponse response = userService.getCurrentUserInfo();
        return ApiResponse.success("获取成功", response);
    }

    /**
     * 修改当前登录用户个人资料
     * PUT /api/user/profile
     */
    @PutMapping("/profile")
    public ApiResponse<UserInfoResponse> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request) {

        UserInfoResponse response = userService.updateProfile(request);
        return ApiResponse.success("修改成功", response);
    }

    /**
     * 修改当前登录用户密码
     * PUT /api/user/password
     */
    @PutMapping("/password")
    public ApiResponse<Void> updatePassword(
            @Valid @RequestBody UpdatePasswordRequest request) {

        userService.updatePassword(request);
        return ApiResponse.success("密码修改成功", null);
    }

    /**
     * 获取当前用户算力点余额
     * GET /api/user/points
     */
    @GetMapping("/points")
    public ApiResponse<Integer> getCurrentUserPoints() {
        Integer points = userService.getCurrentUserPoints();
        return ApiResponse.success("获取成功", points);
    }

    /**
     * 当前用户每日签到领取算力点
     * POST /api/user/sign-in
     */
    @PostMapping("/sign-in")
    public ApiResponse<SignInResponse> signIn() {

        SignInResponse response = userService.signIn();

        return ApiResponse.success("签到成功", response);
    }
}
