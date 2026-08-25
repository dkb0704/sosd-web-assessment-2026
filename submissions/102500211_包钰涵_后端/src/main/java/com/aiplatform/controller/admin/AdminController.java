package com.aiplatform.controller.admin;

import com.aiplatform.common.BusinessException;
import com.aiplatform.common.Result;
import com.aiplatform.dto.admin.AdminUserUpdateRequest;
import com.aiplatform.dto.admin.PointsModifyRequest;
import com.aiplatform.service.admin.AdminService;
import com.aiplatform.service.order.OrderService;
import com.aiplatform.service.creation.CreationService;
import com.aiplatform.mapper.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final OrderService orderService;
    // 直接注入mapper用于简单管理（也可以封装到service）
    private final ProductMapper productMapper;
    private final AiModelMapper modelMapper;

    // ============ 用户管理 ============
    @GetMapping("/user/list")
    public Result<?> userList(@RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "20") int size,
                              @RequestParam(required = false) String nickname,
                              HttpServletRequest request) {
        checkAdmin(request);
        return Result.success(adminService.pageUsers(page, size, nickname));
    }

    @GetMapping("/user/{userId}")
    public Result<?> userDetail(@PathVariable Long userId, HttpServletRequest request) {
        checkAdmin(request);
        return Result.success(adminService.getUserDetail(userId));
    }

    @PutMapping("/user/{userId}")
    public Result<?> updateUser(@PathVariable Long userId,
                                @RequestBody AdminUserUpdateRequest req,
                                HttpServletRequest request) {
        checkAdmin(request);
        adminService.updateUser(userId, req);
        return Result.success();
    }

    @PutMapping("/user/{userId}/status")
    public Result<?> updateUserStatus(@PathVariable Long userId,
                                      @RequestBody Map<String, Integer> body,
                                      HttpServletRequest request) {
        checkAdmin(request);
        adminService.updateUserStatus(userId, body.get("status"));
        return Result.success();
    }

    @PutMapping("/user/points")
    public Result<?> modifyPoints(@Valid @RequestBody PointsModifyRequest req,
                                  HttpServletRequest request) {
        checkAdmin(request);
        adminService.modifyPoints(req);
        return Result.success();
    }

    // ============ 商品管理 ============
    @PostMapping("/product")
    public Result<?> addProduct(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        checkAdmin(request);
        // 简单实现
        // ...
        return Result.success();
    }
    // 省略类似接口，它们可直接调用mapper或service

    // ============ 模型管理 ============
    @GetMapping("/model/list")
    public Result<?> modelList(HttpServletRequest request) {
        checkAdmin(request);
        return Result.success(modelMapper.selectList(null));
    }
    // 其他模型管理类似

    // ============ 任务与订单管理 ============
    @GetMapping("/task/list")
    public Result<?> taskList(@RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "20") int size,
                              @RequestParam(required = false) Integer status,
                              @RequestParam(required = false) Long userId,
                              HttpServletRequest request) {
        checkAdmin(request);
        // 调用creationService或其他查询（省略完整实现）
        return Result.success();
    }

    @GetMapping("/order/list")
    public Result<?> orderList(@RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "20") int size,
                               @RequestParam(required = false) Integer status,
                               @RequestParam(required = false) Long userId,
                               HttpServletRequest request) {
        checkAdmin(request);
        // 类似实现
        return Result.success();
    }

    @PutMapping("/order/{orderNo}/status")
    public Result<?> updateOrderStatus(@PathVariable String orderNo,
                                       @RequestBody Map<String, Integer> body,
                                       HttpServletRequest request) {
        checkAdmin(request);
        orderService.updateOrderStatus(orderNo, body.get("status"));
        return Result.success();
    }

    // 管理员权限校验
    private void checkAdmin(HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"admin".equals(role)) {
            throw new BusinessException(403, "无管理员权限");
        }
    }
}