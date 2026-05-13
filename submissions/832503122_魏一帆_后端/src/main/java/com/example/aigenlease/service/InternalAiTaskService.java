package com.example.aigenlease.service;

import com.example.aigenlease.common.ApiResponse;

import com.example.aigenlease.dto.ai.request.*;
import com.example.aigenlease.dto.ai.response.*;

public interface InternalAiTaskService {

    ApiResponse<Void> completeTask(
            Long taskId,
            CompleteAiTaskRequest req
    );

    ApiResponse<Void> failTask(
            Long taskId,
            FailAiTaskRequest req
    );
}