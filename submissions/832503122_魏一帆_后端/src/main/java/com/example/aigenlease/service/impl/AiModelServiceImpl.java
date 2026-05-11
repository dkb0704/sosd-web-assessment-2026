package com.example.aigenlease.service.impl;

import com.example.aigenlease.common.ApiResponse;

import com.example.aigenlease.dto.ai.request.*;
import com.example.aigenlease.dto.ai.response.*;

import com.example.aigenlease.service.AiModelService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiModelServiceImpl implements AiModelService {

    @Override
    public ApiResponse<List<AiModelResponse>> getModels() {

        List<AiModelResponse> response = List.of(
                new AiModelResponse(
                        1L,
                        "GPT-4o",
                        "OpenAI 最新多模态模型",
                        20,
                        true
                ),
                new AiModelResponse(
                        2L,
                        "SDXL",
                        "高质量 AI 绘图模型",
                        10,
                        true
                )
        );

        return ApiResponse.success("获取模型列表成功", response);
    }

    @Override
    public ApiResponse<AiModelDetailResponse> getModelDetail(
            Long modelId
    ) {

        AiModelDetailResponse response =
                new AiModelDetailResponse(
                        modelId,
                        "GPT-4o",
                        "OpenAI 最新多模态模型",
                        20,
                        true,
                        128000,
                        true,
                        true
                );

        return ApiResponse.success("获取模型详情成功", response);
    }
}