package com.example.aigenlease.controller;

import com.example.aigenlease.common.ApiResponse;

import com.example.aigenlease.dto.ai.request.*;
import com.example.aigenlease.dto.ai.response.*;

import com.example.aigenlease.service.AiModelService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ai/models")
public class AiModelController {

    @Autowired
    private AiModelService aiModelService;

    @GetMapping
    public ApiResponse<List<AiModelResponse>> getModels() {
        return aiModelService.getModels();
    }

    @GetMapping("/{modelId}")
    public ApiResponse<AiModelDetailResponse> getModelDetail(
            @PathVariable Long modelId
    ) {
        return aiModelService.getModelDetail(modelId);
    }
}