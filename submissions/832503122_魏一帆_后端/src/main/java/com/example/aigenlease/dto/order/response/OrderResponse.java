package com.example.aigenlease.dto.order.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResponse(
        Long orderId,
        String orderNo,
        String productName,
        Integer computePoints,
        BigDecimal amount,
        String status,
        LocalDateTime createdAt
) {
}