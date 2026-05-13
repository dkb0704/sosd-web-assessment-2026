package com.example.aigenlease.dto.ai.response;

public record AiTaskStatusResponse(
        Long taskId,
        String status,
        Integer progress,
        String resultUrl
) {
}