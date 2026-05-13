package com.example.aigenlease.dto.order.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderDetailResponse(
        Long orderId,
        String orderNo,
        Long productId,
        String productName,
        Integer computePoints,
        BigDecimal amount,
        String status,
        LocalDateTime createdAt,
        LocalDateTime paidAt
) {
}