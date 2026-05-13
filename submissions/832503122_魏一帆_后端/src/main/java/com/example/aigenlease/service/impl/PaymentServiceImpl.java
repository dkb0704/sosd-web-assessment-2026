package com.example.aigenlease.service.impl;

import com.example.aigenlease.common.ApiResponse;

import com.example.aigenlease.dto.payment.request.*;
import com.example.aigenlease.dto.payment.response.*;

import com.example.aigenlease.service.PaymentService;

import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Override
    public String alipayNotify(
            AlipayNotifyRequest req
    ) {
        return "success";
    }

    @Override
    public ApiResponse<AlipayReturnResponse> alipayReturn(
            AlipayReturnRequest req
    ) {

        AlipayReturnResponse response =
                new AlipayReturnResponse(
                        req.out_trade_no(),
                        "TRADE_SUCCESS"
                );

        return ApiResponse.success("支付成功", response);
    }
}