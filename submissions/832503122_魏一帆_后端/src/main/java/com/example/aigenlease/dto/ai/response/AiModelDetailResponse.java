package com.example.aigenlease.dto.ai.response;

public record AiModelDetailResponse(
        Long modelId,
        String modelName,
        String description,
        Integer costPoints,
        Boolean enabled,
        Integer maxTokens,
        Boolean supportsImage,
        Boolean supportsText
) {
}