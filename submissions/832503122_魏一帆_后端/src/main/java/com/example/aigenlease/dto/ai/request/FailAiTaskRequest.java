package com.example.aigenlease.dto.ai.request;

import jakarta.validation.constraints.NotBlank;

public record FailAiTaskRequest(

        @NotBlank(message = "失败原因不能为空")
        String reason

) {
}