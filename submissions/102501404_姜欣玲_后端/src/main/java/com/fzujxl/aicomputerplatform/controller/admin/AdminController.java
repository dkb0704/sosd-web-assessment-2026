package com.fzujxl.aicomputerplatform.controller.admin;

import com.fzujxl.aicomputerplatform.common.Result;
import com.fzujxl.aicomputerplatform.dto.PageRequest;
import com.fzujxl.aicomputerplatform.dto.PageResultResponse;
import com.fzujxl.aicomputerplatform.dto.admin.*;
import com.fzujxl.aicomputerplatform.dto.admin.order.OrderOperateRequest;
import com.fzujxl.aicomputerplatform.dto.admin.order.OrderOperateResponse;
import com.fzujxl.aicomputerplatform.dto.user.UserProfileResponse;
import com.fzujxl.aicomputerplatform.entity.User;
import com.fzujxl.aicomputerplatform.service.admin.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@Slf4j
@Tag(name = "管理员模块")

public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/queryAllUsersInfo")
    @Operation(summary = "分页查询所有用户信息")
    public Result<PageResultResponse<User>> queryAllUsersInfo( @Valid @RequestBody PageRequest request) {
        log.info("分页查询用户信息,request:{}", request);
        PageResultResponse<User> response = adminService.queryUsersInfo(request.getPageNum(), request.getPageSize());
        return Result.success("查询成功", response);
    }

    @GetMapping("/queryUserInfo")
    @Operation(summary = "查询用户信息")
    public Result<UserProfileResponse> queryUserInfo( @Valid @RequestBody UserInfoQueryRequest request) {
        log.info("查询用户信息,request:{}", request);
        UserProfileResponse response = adminService.queryUserInfo(request);
        return Result.success("查询成功", response);
    }

    @PostMapping("/updateUserInfo")
    @Operation(summary = "修改用户信息")
    public Result<UserProfileResponse> updateUserInfo( @Valid @RequestBody AdminUpdateRequest request) {
        log.info("修改用户信息,request:{}", request);
        UserProfileResponse response = adminService.updateUserInfo(request);
        return Result.success("修改成功", response);
    }

    @PostMapping("/updateUserStatus")
    @Operation(summary = "启用/禁用用户")
    public Result<UserStatusOperationResponse> operateUserStatus( @Valid @RequestBody UserStatusOperateRequest request) {
        log.info("启用/禁用用户,request:{}", request);
        UserStatusOperationResponse response = adminService.operateUserStatus(request);
        return Result.success("操作成功", response);
    }

    @PostMapping("/operateUserPoint")
    @Operation(summary = "操作用户积分")
    public Result<PointOperateResponse> operateUserPoint( @Valid @RequestBody PointOperateRequest request) {
        log.info("操作用户积分,request:{}", request);
        PointOperateResponse response = adminService.operateUserPoint(request);
        return Result.success("操作成功", response);
    }

    @PostMapping("/operateOrder")
    @Operation(summary = "操作异常充值订单")
    public Result<OrderOperateResponse> operateOrder(@Valid @RequestBody OrderOperateRequest request) {
        log.info("操作异常充值订单,request:{}", request);
        OrderOperateResponse response = adminService.operateOrder(request);
        return Result.success("操作成功", response);
    }
}