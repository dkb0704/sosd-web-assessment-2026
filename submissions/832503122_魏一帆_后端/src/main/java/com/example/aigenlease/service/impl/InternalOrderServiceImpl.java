package com.example.aigenlease.service.impl;

import com.example.aigenlease.common.ApiResponse;

import com.example.aigenlease.dto.order.request.*;
import com.example.aigenlease.dto.order.response.*;

import com.example.aigenlease.service.InternalOrderService;

import org.springframework.stereotype.Service;

@Service
public class InternalOrderServiceImpl implements InternalOrderService {

    @Override
    public ApiResponse<Void> syncPayment(
            Long orderId
    ) {

        return ApiResponse.success("同步支付状态成功", null);
    }

    @Override
    public ApiResponse<Void> closeTimeoutOrders() {

        return ApiResponse.success("关闭超时订单成功", null);
    }

    @Override
    public ApiResponse<Void> compensateMissingPayments() {

        return ApiResponse.success("补偿遗漏支付订单成功", null);
    }
}