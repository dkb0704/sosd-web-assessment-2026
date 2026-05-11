package com.example.aigenlease.controller;

import com.example.aigenlease.common.ApiResponse;
import com.example.aigenlease.dto.ai.request.*;
import com.example.aigenlease.dto.ai.response.*;
import com.example.aigenlease.common.PageResponse;
import com.example.aigenlease.service.AiTaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai/tasks")
public class AiTaskController {

    @Autowired
    private AiTaskService aiTaskService;

    @PostMapping
    public ApiResponse<CreateAiTaskResponse> createTask(
            @RequestAttribute("userId") Long userId,
            @Valid @RequestBody CreateAiTaskRequest req
    ) {
        return aiTaskService.createTask(userId, req);
    }

    @GetMapping
    public ApiResponse<PageResponse<AiTaskResponse>> getTasks(
            @RequestAttribute("userId") Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return aiTaskService.getTasks(userId, page, size);
    }

    @GetMapping("/{taskId}")
    public ApiResponse<AiTaskDetailResponse> getTaskDetail(
            @PathVariable Long taskId,
            @RequestAttribute("userId") Long userId
    ) {
        return aiTaskService.getTaskDetail(taskId, userId);
    }

    @GetMapping("/{taskId}/status")
    public ApiResponse<AiTaskStatusResponse> getTaskStatus(
            @PathVariable Long taskId,
            @RequestAttribute("userId") Long userId
    ) {
        return aiTaskService.getTaskStatus(taskId, userId);
    }

    @DeleteMapping("/{taskId}")
    public ApiResponse<Void> deleteTask(
            @PathVariable Long taskId,
            @RequestAttribute("userId") Long userId
    ) {
        return aiTaskService.deleteTask(taskId, userId);
    }

    @PatchMapping("/{taskId}/publish")
    public ApiResponse<Void> publishTask(
            @PathVariable Long taskId,
            @RequestAttribute("userId") Long userId
    ) {
        return aiTaskService.publishTask(taskId, userId);
    }

    @PatchMapping("/{taskId}/unpublish")
    public ApiResponse<Void> unpublishTask(
            @PathVariable Long taskId,
            @RequestAttribute("userId") Long userId
    ) {
        return aiTaskService.unpublishTask(taskId, userId);
    }
}