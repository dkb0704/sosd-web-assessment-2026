package com.fzujxl.aicomputerplatform.service.admin;

import com.fzujxl.aicomputerplatform.dto.PageResultResponse;
import com.fzujxl.aicomputerplatform.dto.admin.*;
import com.fzujxl.aicomputerplatform.dto.admin.order.OrderOperateRequest;
import com.fzujxl.aicomputerplatform.dto.admin.order.OrderOperateResponse;
import com.fzujxl.aicomputerplatform.dto.user.UserProfileResponse;
import com.fzujxl.aicomputerplatform.entity.User;

public interface AdminService {

     PageResultResponse<User> queryUsersInfo(int pageNum, int pageSize);

     UserProfileResponse queryUserInfo(UserInfoQueryRequest request);

     UserProfileResponse updateUserInfo(AdminUpdateRequest request);

     UserStatusOperationResponse operateUserStatus(UserStatusOperateRequest request);

     PointOperateResponse operateUserPoint(PointOperateRequest request);

     OrderOperateResponse operateOrder(OrderOperateRequest request);
}
