package com.example.aigenlease.controller;

import com.example.aigenlease.common.ApiResponse;

import com.example.aigenlease.dto.notification.request.*;

import com.example.aigenlease.service.InternalNotificationService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/internal/notifications")
public class InternalNotificationController {

    @Autowired
    private InternalNotificationService internalNotificationService;

    @PostMapping("/email")
    public ApiResponse<Void> sendEmail(
            @Valid @RequestBody SendEmailRequest req
    ) {
        return internalNotificationService.sendEmail(req);
    }
}