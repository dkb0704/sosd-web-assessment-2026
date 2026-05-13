package com.example.aigenlease.service;

import com.example.aigenlease.common.ApiResponse;

import com.example.aigenlease.dto.notification.request.*;

public interface InternalNotificationService {

    ApiResponse<Void> sendEmail(
            SendEmailRequest req
    );
}