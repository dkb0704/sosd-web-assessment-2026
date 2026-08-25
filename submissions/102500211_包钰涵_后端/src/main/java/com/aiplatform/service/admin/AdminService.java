package com.aiplatform.service.admin;

import com.aiplatform.dto.admin.AdminUserUpdateRequest;
import com.aiplatform.dto.admin.PointsModifyRequest;
import com.aiplatform.dto.user.UserInfoResponse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface AdminService {
    Page<UserInfoResponse> pageUsers(int page, int size, String nickname);
    UserInfoResponse getUserDetail(Long userId);
    void updateUser(Long userId, AdminUserUpdateRequest request);
    void updateUserStatus(Long userId, Integer status);
    void modifyPoints(PointsModifyRequest request);
}