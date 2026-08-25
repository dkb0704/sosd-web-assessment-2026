package com.aiplatform.service.user;

import com.aiplatform.dto.user.UserInfoResponse;
import com.aiplatform.dto.user.UpdateUserInfoRequest;

public interface UserService {
    UserInfoResponse getCurrentUser(Long userId);
    void updateUserInfo(Long userId, UpdateUserInfoRequest request);
    int dailySign(Long userId);
    int getPoints(Long userId);

}