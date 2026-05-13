package com.example.aigenlease.service.impl;

import com.example.aigenlease.common.ApiResponse;

import com.example.aigenlease.dto.notification.request.*;

import com.example.aigenlease.service.InternalNotificationService;

import org.springframework.stereotype.Service;

@Service
public class InternalNotificationServiceImpl implements InternalNotificationService {

    @Override
    public ApiResponse<Void> sendEmail(
            SendEmailRequest req
    ) {

        return ApiResponse.success("邮件发送成功", null);
    }
}