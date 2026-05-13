package com.example.aigenlease.dto.ai.response;

import java.time.LocalDateTime;

public record AiTaskResponse(
        Long taskId,
        String prompt,
        String status,
        String resultUrl,
        Boolean published,
        LocalDateTime createdAt
) {
}