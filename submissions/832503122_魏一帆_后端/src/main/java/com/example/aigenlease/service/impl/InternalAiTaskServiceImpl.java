package com.example.aigenlease.service.impl;

import com.example.aigenlease.common.ApiResponse;

import com.example.aigenlease.dto.ai.request.*;
import com.example.aigenlease.dto.ai.response.*;

import com.example.aigenlease.service.InternalAiTaskService;

import org.springframework.stereotype.Service;

@Service
public class InternalAiTaskServiceImpl implements InternalAiTaskService {

    @Override
    public ApiResponse<Void> completeTask(
            Long taskId,
            CompleteAiTaskRequest req
    ) {

        return ApiResponse.success("任务完成成功", null);
    }

    @Override
    public ApiResponse<Void> failTask(
            Long taskId,
            FailAiTaskRequest req
    ) {

        return ApiResponse.success("任务失败状态更新成功", null);
    }
}