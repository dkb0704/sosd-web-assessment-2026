package com.example.aigenlease.service.impl;

import org.springframework.stereotype.Service;

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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import com.example.aigenlease.common.PageResponse;

@Service
public class UserServiceImpl implements UserService{
    @Override
    public ApiResponse<UserRegisterResponse> register(UserRegisterRequest req) {
        UserRegisterResponse response = new UserRegisterResponse(
                1L,
                req.username()
        );

        return ApiResponse.success("注册成功", response);
    }

    @Override
    public ApiResponse<UserLoginResponse> login(UserLoginRequest req) {
        UserLoginResponse response = new UserLoginResponse(
            1L,
            req.username(),
            "token"
        );
        return ApiResponse.success("登录成功", response);
    }

    @Override
    public ApiResponse<Void> logout(Long userId) {
        return ApiResponse.success("退出登录成功", null);
    }

    @Override
    public ApiResponse<UserInfoResponse> getUserInfo(Long userId) {
        UserInfoResponse response = new UserInfoResponse(
            userId,
            "testUser"
        );
        return ApiResponse.success("获取用户信息成功", response);
    }

    @Override
    public ApiResponse<UserInfoResponse> updateUserInfo(Long userId, UserUpdateRequest req) {
        UserInfoResponse response = new UserInfoResponse(
            userId,
            req.username()
        );
        return ApiResponse.success("更新用户信息成功", response);
    }

    @Override
    public ApiResponse<CheckInResponse> checkIn(Long userId) {
        boolean alreadyCheckedIn = false;
        if (alreadyCheckedIn) {
            return ApiResponse.error(400, "今日已签到");
        }
        CheckInResponse response = new CheckInResponse(
                true,
                10,   
                100   
        );
        return ApiResponse.success("签到成功", response);
    }

    @Override
    public ApiResponse<CheckInStatusResponse> getCheckInStatus(Long userId) {

        CheckInStatusResponse response = new CheckInStatusResponse(
                false,                  
                LocalDate.now().minusDays(1),
                3                       
        );

        return ApiResponse.success("获取签到状态成功", response);
    }

    @Override
    public ApiResponse<PageResponse<PointsLogResponse>> getPointsLogs(
            Long userId, int page, int size
    ) {

        List<PointsLogResponse> list = List.of(
                new PointsLogResponse(10, "CHECK_IN", "每日签到", LocalDateTime.now()),
                new PointsLogResponse(-5, "CONSUME", "兑换商品", LocalDateTime.now())
        );

        PageResponse<PointsLogResponse> response =
                new PageResponse<>(list, page, size, 2);

        return ApiResponse.success("获取积分记录成功", response);
    }
}
