package com.example.aigenlease.dto.order.response;

public record PayOrderResponse(
        Long orderId,
        String orderNo,
        String payUrl,
        String status
) {
}