package com.example.aigenlease.service;

import com.example.aigenlease.common.ApiResponse;
import com.example.aigenlease.dto.user.request.UserLoginRequest;
import com.example.aigenlease.dto.user.request.UserRegisterRequest;
import com.example.aigenlease.dto.user.request.UserUpdateRequest;
import com.example.aigenlease.dto.user.response.UserRegisterResponse;
import com.example.aigenlease.dto.user.response.CheckInResponse;
import com.example.aigenlease.dto.user.response.CheckInStatusResponse;
import com.example.aigenlease.dto.user.response.PointsLogResponse;
import com.example.aigenlease.dto.user.response.UserInfoResponse;
import com.example.aigenlease.dto.user.response.UserLoginResponse;
import com.example.aigenlease.common.PageResponse;

public interface UserService {
    ApiResponse<UserRegisterResponse> register(UserRegisterRequest req);
    ApiResponse<UserLoginResponse> login(UserLoginRequest req);
    ApiResponse<Void> logout(Long userId);
    ApiResponse<UserInfoResponse> getUserInfo(Long userId);
    ApiResponse<UserInfoResponse> updateUserInfo(Long userId, UserUpdateRequest req);
    ApiResponse<CheckInResponse> checkIn(Long userId);
    ApiResponse<CheckInStatusResponse> getCheckInStatus(Long userId);
    ApiResponse<PageResponse<PointsLogResponse>> getPointsLogs(Long userId, int page, int size);
}   
