package com.example.aigenlease.dto.ai.response;

import java.time.LocalDateTime;

public record AiTaskDetailResponse(
        Long taskId,
        String prompt,
        String status,
        String resultUrl,
        Long modelId,
        String modelName,
        Integer costPoints,
        Boolean published,
        LocalDateTime createdAt
) {
}