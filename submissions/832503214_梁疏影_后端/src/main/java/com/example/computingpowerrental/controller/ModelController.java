package com.example.computingpowerrental.controller;

import com.example.computingpowerrental.dto.AiModelSimpleResponse;
import com.example.computingpowerrental.dto.ApiResponse;
import com.example.computingpowerrental.entity.AiModel;
import com.example.computingpowerrental.service.AiModelService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Lark
 * @ date 2026/8/6  16:42
 * @ description 用户端AI模型接口，提供用户选择AI模型的能力
 */
@RestController
@RequestMapping("/api/models")
public class ModelController {
    private final AiModelService aiModelService;

    public ModelController(AiModelService aiModelService) {
        this.aiModelService = aiModelService;
    }

    //查询当前可用模型
    @GetMapping
    public ApiResponse<List<AiModelSimpleResponse>> listPublished() {

        List<AiModel> models = aiModelService.listPublished();

        List<AiModelSimpleResponse> responses = models.stream().map(
                model ->
                                new AiModelSimpleResponse(
                                        model.getId(),
                                        model.getModelName(),
                                        model.getProvider(),
                                        model.getCategory(),
                                        model.getCostPoints(),
                                        model.getDescription()
                                )
                        ).collect(Collectors.toList());

        return ApiResponse.success(
                "查询可用模型成功",
                responses
        );

    }
}
