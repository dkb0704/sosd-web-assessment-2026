package com.example.computingpowerrental.controller;

import com.example.computingpowerrental.dto.AdminAdjustPointsRequest;
import com.example.computingpowerrental.dto.AdminUpdateUserRequest;
import com.example.computingpowerrental.dto.ApiResponse;
import com.example.computingpowerrental.dto.UserInfoResponse;
import com.example.computingpowerrental.service.UserService;
import com.example.computingpowerrental.vo.AdminUserPageVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @author Lark
 * @ date 2026/9/2  22:52
 * @ description 管理端用户管理接口
 */
@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {
    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    //分页查询用户
    @GetMapping
    public ApiResponse<AdminUserPageVO> pageUsers(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestParam(required = false) Integer status) {

        return ApiResponse.success("查询用户列表成功", userService.adminPageUsers(page, size, status));
    }

    //查询用户详情
    @GetMapping("/{id}")
    public ApiResponse<UserInfoResponse> detail(@PathVariable Long id) {

        return ApiResponse.success(
                "查询用户详情成功",
                userService.adminGetUser(id)
        );
    }

    //修改用户资料
    @PutMapping("/{id}")
    public ApiResponse<UserInfoResponse> update(@PathVariable Long id, @RequestBody AdminUpdateUserRequest request) {

        return ApiResponse.success(
                "修改用户信息成功",
                userService.adminUpdateUser(id, request)
        );
    }

    //启用用户
    @PutMapping("/{id}/enable")
    public ApiResponse<Void> enable(@PathVariable Long id) {

        userService.adminUpdateStatus(id, 1);

        return ApiResponse.success("启用用户成功");
    }

    //禁用用户
    @PutMapping("/{id}/disable")
    public ApiResponse<Void> disable(@PathVariable Long id) {

        userService.adminUpdateStatus(id, 0);

        return ApiResponse.success("禁用用户成功");
    }

    //手动补偿算力
    @PostMapping("/{id}/points/add")
    public ApiResponse<Void> addPoints(@PathVariable Long id, @Valid @RequestBody AdminAdjustPointsRequest request) {

        userService.adminAddPoints(id, request.getPoints());

        return ApiResponse.success("补偿用户算力成功");
    }

    //手动扣除算力
    @PostMapping("/{id}/points/deduct")
    public ApiResponse<Void> deductPoints(@PathVariable Long id, @Valid @RequestBody AdminAdjustPointsRequest request) {

        userService.adminDeductPoints(id, request.getPoints());

        return ApiResponse.success("扣除用户算力成功");
    }
}
