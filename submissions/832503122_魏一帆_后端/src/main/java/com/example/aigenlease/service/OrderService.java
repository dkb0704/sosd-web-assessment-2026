package com.example.aigenlease.service;

import com.example.aigenlease.common.ApiResponse;
import com.example.aigenlease.common.PageResponse;

import com.example.aigenlease.dto.order.request.*;
import com.example.aigenlease.dto.order.response.*;

public interface OrderService {

    ApiResponse<PageResponse<OrderResponse>> getOrders(
            Long userId,
            int page,
            int size
    );

    ApiResponse<CreateOrderResponse> createOrder(
            Long userId,
            CreateOrderRequest req
    );

    ApiResponse<OrderDetailResponse> getOrderDetail(
            Long orderId,
            Long userId
    );

    ApiResponse<PayOrderResponse> payOrder(
            Long orderId,
            Long userId
    );

    ApiResponse<Void> closeOrder(
            Long orderId,
            Long userId
    );

    ApiResponse<OrderPaymentStatusResponse> getPaymentStatus(
            Long orderId,
            Long userId
    );
}