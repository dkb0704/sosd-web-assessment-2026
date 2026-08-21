package com.fzujxl.aicomputerplatform.controller.user;

import com.fzujxl.aicomputerplatform.common.Result;

import com.fzujxl.aicomputerplatform.dto.user.*;
import com.fzujxl.aicomputerplatform.dto.sign.SignResponse;
import com.fzujxl.aicomputerplatform.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Slf4j
@Tag(name = "用户模块")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    Result<AuthResponse>login(@Valid @RequestBody LoginRequest loginRequest){
        log.info("收到登录请求： account={}", loginRequest.getAccount());
        AuthResponse Response = userService.login(loginRequest);
        return Result.success("登录成功", Response);
    }

    @GetMapping("/me")
    @Operation(summary = "获取当前用户/管理员信息")
    public Result<UserProfileResponse> getCurrentUserInfo(@RequestAttribute("userId") Long userId) {
        log.info("获取当前用户/管理员信息，userId: {}", userId);
        UserProfileResponse response = userService.getCurrentUserInfo(userId);
        return Result.success("获取成功", response);
    }

    @PutMapping("/profile")
    @Operation(summary = "修改个人信息")
    public Result<UserProfileResponse> updateProfile(@RequestAttribute("userId") Long userId,
                                                     @RequestBody @Valid UpdateProfileRequest request) {
        log.info("修改个人信息，userId: {}, request: {}", userId, request);
        UserProfileResponse response = userService.updateProfile(userId, request);
        String message = response.isUpdated() ? "修改成功" : "修改失败";
        return Result.success(message, response);
    }

    @PutMapping("/password")
    @Operation(summary = "修改密码")
    public Result<Void> changePassword(@RequestAttribute("userId") Long userId,
                                       @Valid @RequestBody ChangePasswordRequest request,
                                       HttpServletRequest httpRequest) {
        String authHeader = httpRequest.getHeader("Authorization");
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // 去除 "Bearer " 前缀
        }

        log.info("修改密码，userId: {}", userId);
        userService.changePassword(userId, request, token);
        return Result.success("密码修改成功");
    }

    @PostMapping("/sign")
    @Operation(summary = "每日签到")
    public Result<SignResponse> sign(@RequestAttribute("userId") Long userId) {
        log.info("用户签到，userId: {}", userId);
        SignResponse response = userService.sign(userId);
        return Result.success("签到成功", response);
    }


    @PostMapping("/logout")
    @Operation(summary = "退出登录")
    public Result<Void> logout(HttpServletRequest httpRequest) {
        String authHeader = httpRequest.getHeader("Authorization");
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // 去除 "Bearer " 前缀
        }

        log.info("用户退出登录");
        userService.logout(token);
        return Result.success("退出成功");
    }
}