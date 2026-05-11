package com.example.aigenlease.service;

import com.example.aigenlease.common.ApiResponse;

import com.example.aigenlease.dto.order.request.*;
import com.example.aigenlease.dto.order.response.*;

public interface InternalOrderService {

    ApiResponse<Void> syncPayment(Long orderId);
    ApiResponse<Void> closeTimeoutOrders();
    ApiResponse<Void> compensateMissingPayments();
}