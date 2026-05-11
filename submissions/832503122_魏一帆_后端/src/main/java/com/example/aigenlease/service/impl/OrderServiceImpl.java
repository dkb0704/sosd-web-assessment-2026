package com.example.aigenlease.service.impl;

import com.example.aigenlease.common.ApiResponse;
import com.example.aigenlease.common.PageResponse;

import com.example.aigenlease.dto.order.request.*;
import com.example.aigenlease.dto.order.response.*;

import com.example.aigenlease.service.OrderService;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Override
    public ApiResponse<PageResponse<OrderResponse>> getOrders(
            Long userId,
            int page,
            int size
    ) {

        List<OrderResponse> records = List.of(
                new OrderResponse(
                        1L,
                        "ORDER202605090001",
                        "100算力包",
                        100,
                        new BigDecimal("9.90"),
                        "PAID",
                        LocalDateTime.now()
                ),
                new OrderResponse(
                        2L,
                        "ORDER202605090002",
                        "500算力包",
                        500,
                        new BigDecimal("39.90"),
                        "PENDING",
                        LocalDateTime.now()
                )
        );

        PageResponse<OrderResponse> response =
                new PageResponse<>(
                        records,
                        page,
                        size,
                        records.size()
                );

        return ApiResponse.success("获取订单列表成功", response);
    }

    @Override
    public ApiResponse<CreateOrderResponse> createOrder(
            Long userId,
            CreateOrderRequest req
    ) {

        CreateOrderResponse response =
                new CreateOrderResponse(
                        1L,
                        "ORDER202605090001",
                        new BigDecimal("39.90"),
                        "PENDING"
                );

        return ApiResponse.success("创建订单成功", response);
    }

    @Override
    public ApiResponse<OrderDetailResponse> getOrderDetail(
            Long orderId,
            Long userId
    ) {

        OrderDetailResponse response =
                new OrderDetailResponse(
                        orderId,
                        "ORDER202605090001",
                        1L,
                        "500算力包",
                        500,
                        new BigDecimal("39.90"),
                        "PAID",
                        LocalDateTime.now(),
                        LocalDateTime.now()
                );

        return ApiResponse.success("获取订单详情成功", response);
    }

    @Override
    public ApiResponse<PayOrderResponse> payOrder(
            Long orderId,
            Long userId
    ) {

        PayOrderResponse response =
                new PayOrderResponse(
                        orderId,
                        "ORDER202605090001",
                        "https://example.com/pay/qrcode",
                        "PENDING"
                );

        return ApiResponse.success("发起支付成功", response);
    }

    @Override
    public ApiResponse<Void> closeOrder(
            Long orderId,
            Long userId
    ) {

        return ApiResponse.success("关闭订单成功", null);
    }

    @Override
    public ApiResponse<OrderPaymentStatusResponse> getPaymentStatus(
            Long orderId,
            Long userId
    ) {

        OrderPaymentStatusResponse response =
                new OrderPaymentStatusResponse(
                        orderId,
                        "ORDER202605090001",
                        "TRADE_SUCCESS"
                );

        return ApiResponse.success("获取支付状态成功", response);
    }
}