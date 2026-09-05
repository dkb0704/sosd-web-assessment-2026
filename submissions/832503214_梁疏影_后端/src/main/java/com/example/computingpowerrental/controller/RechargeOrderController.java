package com.example.computingpowerrental.controller;

import com.example.computingpowerrental.dto.ApiResponse;
import com.example.computingpowerrental.dto.CreateRechargeOrderRequest;
import com.example.computingpowerrental.entity.RechargeOrder;
import com.example.computingpowerrental.service.RechargeOrderService;
import com.example.computingpowerrental.util.RequestHolder;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import com.example.computingpowerrental.vo.RechargeOrderPageVO;

/**
 * @author Lark
 * @ date 2026/8/16  15:33
 * @ description 用户充值订单接口
 */
@RestController
@RequestMapping("/api/recharge/orders")
public class RechargeOrderController {
    private final RechargeOrderService rechargeOrderService;

    public RechargeOrderController(RechargeOrderService rechargeOrderService) {
        this.rechargeOrderService = rechargeOrderService;
    }

    //创建充值订单
    @PostMapping
    public ApiResponse<RechargeOrder> createOrder(@Valid @RequestBody CreateRechargeOrderRequest request) {

        Long userId = RequestHolder.getUserId();

        RechargeOrder order = rechargeOrderService.createOrder(userId, request.getPackageId());

        return ApiResponse.success("创建充值订单成功", order);
    }

    //分页查询我的充值订单
    @GetMapping
    public ApiResponse<RechargeOrderPageVO> listMyOrders(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestParam(required = false) Integer status) {

        Long userId = RequestHolder.getUserId();

        return ApiResponse.success(
                "查询充值订单成功",
                rechargeOrderService.pageUserOrders(userId, page, size, status)
        );
    }

    //查询我的充值订单详情
    @GetMapping("/{id}")
    public ApiResponse<RechargeOrder> detail(@PathVariable Long id) {

        Long userId = RequestHolder.getUserId();

        return ApiResponse.success("查询充值订单详情成功", rechargeOrderService.getUserOrder(userId, id));
    }

    //取消充值订单
    @PostMapping("/{id}/cancel")
    public ApiResponse<Void> cancelOrder(@PathVariable Long id) {

        Long userId = RequestHolder.getUserId();

        rechargeOrderService.cancelOrder(userId, id);

        return ApiResponse.success("取消充值订单成功");
    }
}
