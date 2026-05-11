package com.example.aigenlease.controller;

import com.example.aigenlease.common.ApiResponse;
import com.example.aigenlease.common.PageResponse;

import com.example.aigenlease.dto.order.request.*;
import com.example.aigenlease.dto.order.response.*;
import jakarta.validation.Valid;
import com.example.aigenlease.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ApiResponse<PageResponse<OrderResponse>> getOrders(
            @RequestAttribute("userId") Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return orderService.getOrders(userId, page, size);
    }

    @PostMapping
    public ApiResponse<CreateOrderResponse> createOrder(
            @RequestAttribute("userId") Long userId,
            @Valid @RequestBody CreateOrderRequest req
    ) {
        return orderService.createOrder(userId, req);
    }

    @GetMapping("/{orderId}")
    public ApiResponse<OrderDetailResponse> getOrderDetail(
            @PathVariable Long orderId,
            @RequestAttribute("userId") Long userId
    ) {
        return orderService.getOrderDetail(orderId, userId);
    }

    @PostMapping("/{orderId}/pay")
    public ApiResponse<PayOrderResponse> payOrder(
            @PathVariable Long orderId,
            @RequestAttribute("userId") Long userId
    ) {
        return orderService.payOrder(orderId, userId);
    }

    @PostMapping("/{orderId}/close")
    public ApiResponse<Void> closeOrder(
            @PathVariable Long orderId,
            @RequestAttribute("userId") Long userId
    ) {
        return orderService.closeOrder(orderId, userId);
    }

    @GetMapping("/{orderId}/payment-status")
    public ApiResponse<OrderPaymentStatusResponse> getPaymentStatus(
            @PathVariable Long orderId,
            @RequestAttribute("userId") Long userId
    ) {
        return orderService.getPaymentStatus(orderId, userId);
    }

    
}