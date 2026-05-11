package com.example.aigenlease.dto.payment.request;

public record AlipayNotifyRequest(
        String trade_no,
        String out_trade_no,
        String trade_status,
        String total_amount
) {
}