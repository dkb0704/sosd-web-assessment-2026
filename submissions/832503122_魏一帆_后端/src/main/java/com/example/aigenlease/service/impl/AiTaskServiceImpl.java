package com.example.aigenlease.service.impl;

import com.example.aigenlease.common.ApiResponse;
import com.example.aigenlease.dto.ai.request.*;
import com.example.aigenlease.dto.ai.response.*;
import com.example.aigenlease.service.AiTaskService;
import com.example.aigenlease.common.PageResponse;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AiTaskServiceImpl implements AiTaskService {

    @Override
    public ApiResponse<CreateAiTaskResponse> createTask(
            Long userId,
            CreateAiTaskRequest req
    ) {

        CreateAiTaskResponse response =
                new CreateAiTaskResponse(
                        1L,
                        "PENDING"
                );

        return ApiResponse.success("任务已进入队列", response);
    }

    @Override
    public ApiResponse<PageResponse<AiTaskResponse>> getTasks(
            Long userId,
            int page,
            int size
    ) {

        List<AiTaskResponse> records = List.of(
                new AiTaskResponse(
                        1L,
                        "A futuristic cyberpunk city",
                        "SUCCESS",
                        "https://example.com/result-1.png",
                        true,
                        LocalDateTime.now()
                ),
                new AiTaskResponse(
                        2L,
                        "A cute anime girl",
                        "PENDING",
                        null,
                        false,
                        LocalDateTime.now()
                )
        );

        PageResponse<AiTaskResponse> response =
                new PageResponse<>(
                        records,
                        page,
                        size,
                        records.size()
                );

        return ApiResponse.success("获取任务列表成功", response);
    }

    @Override
    public ApiResponse<AiTaskDetailResponse> getTaskDetail(
            Long taskId,
            Long userId
    ) {

        AiTaskDetailResponse response =
                new AiTaskDetailResponse(
                        taskId,
                        "A futuristic cyberpunk city",
                        "SUCCESS",
                        "https://example.com/result.png",
                        1L,
                        "SDXL",
                        20,
                        true,
                        LocalDateTime.now()
                );

        return ApiResponse.success("获取任务详情成功", response);
    }

    @Override
    public ApiResponse<AiTaskStatusResponse> getTaskStatus(
            Long taskId,
            Long userId
    ) {

        AiTaskStatusResponse response =
                new AiTaskStatusResponse(
                        taskId,
                        "PROCESSING",
                        60,
                        null
                );

        return ApiResponse.success("获取任务状态成功", response);
    }

    @Override
    public ApiResponse<Void> deleteTask(
            Long taskId,
            Long userId
    ) {

        return ApiResponse.success("删除任务成功", null);
    }

    @Override
    public ApiResponse<Void> publishTask(
            Long taskId,
            Long userId
    ) {

        return ApiResponse.success("公开作品成功", null);
    }

    @Override
    public ApiResponse<Void> unpublishTask(
            Long taskId,
            Long userId
    ) {

        return ApiResponse.success("取消公开成功", null);
    }
}