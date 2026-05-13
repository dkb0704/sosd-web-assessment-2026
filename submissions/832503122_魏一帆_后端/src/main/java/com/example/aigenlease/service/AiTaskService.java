package com.example.aigenlease.service;

import com.example.aigenlease.common.ApiResponse;
import com.example.aigenlease.common.PageResponse;
import com.example.aigenlease.dto.ai.response.*;
import com.example.aigenlease.dto.ai.request.*;

public interface AiTaskService {
    ApiResponse<CreateAiTaskResponse> createTask(
            Long userId,
            CreateAiTaskRequest req
    );

    ApiResponse<PageResponse<AiTaskResponse>> getTasks(
            Long userId,
            int page,
            int size
    );

    ApiResponse<AiTaskDetailResponse> getTaskDetail(
            Long taskId,
            Long userId
    );

    ApiResponse<AiTaskStatusResponse> getTaskStatus(
            Long taskId,
            Long userId
    );

    ApiResponse<Void> deleteTask(
            Long taskId,
            Long userId
    );

    ApiResponse<Void> publishTask(
            Long taskId,
            Long userId
    );

    ApiResponse<Void> unpublishTask(
            Long taskId,
            Long userId
    );
}