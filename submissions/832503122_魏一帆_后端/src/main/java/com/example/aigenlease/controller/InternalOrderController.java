package com.example.aigenlease.controller;

import com.example.aigenlease.common.ApiResponse;

import com.example.aigenlease.dto.order.request.*;
import com.example.aigenlease.dto.order.response.*;

import com.example.aigenlease.service.InternalOrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/internal/orders")
public class InternalOrderController {

    @Autowired
    private InternalOrderService internalOrderService;

    @PostMapping("/{orderId}/sync-payment")
    public ApiResponse<Void> syncPayment(
            @PathVariable Long orderId
    ) {
        return internalOrderService.syncPayment(orderId);
    }

    @PostMapping("/close-timeout")
    public ApiResponse<Void> closeTimeoutOrders() {
        return internalOrderService.closeTimeoutOrders();
    }

    @PostMapping("/compensate-missing-payments")
    public ApiResponse<Void> compensateMissingPayments() {
        return internalOrderService.compensateMissingPayments();
    }
}