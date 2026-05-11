package com.example.aigenlease.service;

import com.example.aigenlease.common.ApiResponse;
import com.example.aigenlease.dto.payment.request.*;
import com.example.aigenlease.dto.payment.response.*;

public interface PaymentService {

    String alipayNotify(
            AlipayNotifyRequest req
    );

    ApiResponse<AlipayReturnResponse> alipayReturn(
            AlipayReturnRequest req
    );
}