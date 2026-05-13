package com.example.aigenlease.service;

import com.example.aigenlease.common.ApiResponse;

import com.example.aigenlease.dto.ai.request.*;
import com.example.aigenlease.dto.ai.response.*;

import java.util.List;

public interface AiModelService {

    ApiResponse<List<AiModelResponse>> getModels();
    ApiResponse<AiModelDetailResponse> getModelDetail(Long modelId);
}