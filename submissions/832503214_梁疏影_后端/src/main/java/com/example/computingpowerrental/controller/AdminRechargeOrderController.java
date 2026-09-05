package com.example.computingpowerrental.controller;

import com.example.computingpowerrental.dto.ApiResponse;
import com.example.computingpowerrental.entity.RechargeOrder;
import com.example.computingpowerrental.service.RechargeOrderService;
import com.example.computingpowerrental.vo.RechargeOrderPageVO;
import org.springframework.web.bind.annotation.*;

/**
 * @author Lark
 * @ date 2026/9/3  18:30
 * @ description 管理端充值订单管理接口
 */
@RestController
@RequestMapping("/api/admin/recharge-orders")
public class AdminRechargeOrderController {
    private final RechargeOrderService rechargeOrderService;

    public AdminRechargeOrderController(
            RechargeOrderService rechargeOrderService
    ) {
        this.rechargeOrderService = rechargeOrderService;
    }


    //分页查询全站充值订单
    @GetMapping
    public ApiResponse<RechargeOrderPageVO> pageOrders(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer status
    ) {

        return ApiResponse.success(
                "查询充值订单列表成功",
                rechargeOrderService.adminPageOrders(
                        page,
                        size,
                        status
                )
        );
    }


    //查询充值订单详情
    @GetMapping("/{id}")
    public ApiResponse<RechargeOrder> detail(
            @PathVariable Long id
    ) {

        return ApiResponse.success(
                "查询充值订单详情成功",
                rechargeOrderService.adminGetOrder(id)
        );
    }


    //异常订单手动补单
    @PostMapping("/{id}/complete")
    public ApiResponse<Void> complete(
            @PathVariable Long id
    ) {

        rechargeOrderService.adminCompleteOrder(id);

        return ApiResponse.success(
                "手动补单成功"
        );
    }
}
