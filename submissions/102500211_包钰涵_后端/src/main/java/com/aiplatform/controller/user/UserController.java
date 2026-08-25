package com.aiplatform.controller.user;

import com.aiplatform.common.Result;
import com.aiplatform.dto.user.UpdateUserInfoRequest;
import com.aiplatform.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/info")
    public Result<?> getInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(userService.getCurrentUser(userId));
    }

    @PutMapping("/info")
    public Result<?> updateInfo(@RequestBody UpdateUserInfoRequest req, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        userService.updateUserInfo(userId, req);
        return Result.success();
    }

    @PostMapping("/sign")
    public Result<?> sign(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        int points = userService.dailySign(userId);
        return Result.success("签到成功，获得 " + points + " 算力", points);
    }

    @GetMapping("/points")
    public Result<?> getPoints(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        int points = userService.getPoints(userId);
        return Result.success(points);
    }
}