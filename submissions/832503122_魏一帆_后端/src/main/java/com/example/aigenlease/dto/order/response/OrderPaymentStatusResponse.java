package com.example.aigenlease.dto.order.response;

public record OrderPaymentStatusResponse(
        Long orderId,
        String orderNo,
        String paymentStatus
) {
}