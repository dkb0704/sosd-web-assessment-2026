package com.example.aigenlease.dto.ai.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateAiTaskRequest(

        @NotBlank(message = "Prompt不能为空")
        String prompt,

        @NotNull(message = "模型ID不能为空")
        Long modelId,

        Boolean publish
) {
}