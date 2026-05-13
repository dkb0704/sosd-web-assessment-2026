package com.example.aigenlease.dto.ai.response;

public record AiModelResponse(
        Long modelId,
        String modelName,
        String description,
        Integer costPoints,
        Boolean enabled
) {
}