package com.example.aigenlease.dto.payment.response;

public record AlipayReturnResponse(
        String orderNo,
        String paymentStatus
) {
}