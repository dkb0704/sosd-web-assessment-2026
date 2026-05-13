package com.example.aigenlease.controller;

import com.example.aigenlease.common.ApiResponse;

import com.example.aigenlease.dto.payment.request.*;
import com.example.aigenlease.dto.payment.response.*;

import com.example.aigenlease.service.PaymentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/alipay/notify")
    public String alipayNotify(
            @ModelAttribute AlipayNotifyRequest req
    ) {
        return paymentService.alipayNotify(req);
    }

    @GetMapping("/alipay/return")
    public ApiResponse<AlipayReturnResponse> alipayReturn(
            @ModelAttribute AlipayReturnRequest req
    ) {
        return paymentService.alipayReturn(req);
    }
}