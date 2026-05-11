package com.example.aigenlease.dto.payment.request;

public record AlipayReturnRequest(
        String trade_no,
        String out_trade_no,
        String total_amount
) {
}