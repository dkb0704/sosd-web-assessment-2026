package com.example.aigenlease.dto.ai.request;

import jakarta.validation.constraints.NotBlank;

public record CompleteAiTaskRequest(

        @NotBlank(message = "结果URL不能为空")
        String resultUrl

) {
}