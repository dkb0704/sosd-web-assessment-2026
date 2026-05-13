package com.example.aigenlease.dto.order.response;

import java.math.BigDecimal;

public record CreateOrderResponse(
        Long orderId,
        String orderNo,
        BigDecimal amount,
        String status
) {
}