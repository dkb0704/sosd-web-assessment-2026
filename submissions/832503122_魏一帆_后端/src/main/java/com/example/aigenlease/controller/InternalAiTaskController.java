package com.example.aigenlease.controller;

import com.example.aigenlease.common.ApiResponse;

import com.example.aigenlease.dto.ai.request.*;
import com.example.aigenlease.dto.ai.response.*;
import com.example.aigenlease.service.InternalOrderService;
import com.example.aigenlease.service.InternalAiTaskService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/internal/ai-tasks")
public class InternalAiTaskController {

    @Autowired
    private InternalAiTaskService internalAiTaskService;

    @PostMapping("/{taskId}/complete")
    public ApiResponse<Void> completeTask(
            @PathVariable Long taskId,
            @Valid @RequestBody CompleteAiTaskRequest req
    ) {
        return internalAiTaskService.completeTask(taskId, req);
    }

    @PostMapping("/{taskId}/fail")
    public ApiResponse<Void> failTask(
            @PathVariable Long taskId,
            @Valid @RequestBody FailAiTaskRequest req
    ) {
        return internalAiTaskService.failTask(taskId, req);
    }
}