package com.example.computingpowerrental.controller;

import com.example.computingpowerrental.dto.*;
import com.example.computingpowerrental.entity.AiModel;
import com.example.computingpowerrental.service.AiModelService;
import com.example.computingpowerrental.dto.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Lark
 * @ date 2026/7/27  17:19
 * @ description AI模型管理控制器
 */
@RestController
@RequestMapping("/api/admin/models")
public class AdminModelController {
    private final AiModelService aiModelService;


    public AdminModelController(AiModelService aiModelService) {
        this.aiModelService = aiModelService;
    }


    //查询全部模型
    @GetMapping
    public ApiResponse<List<AiModel>> list() {

        return ApiResponse.success(
                "查询模型列表成功",
                aiModelService.listAll()
        );

    }



    //查询模型详情
    @GetMapping("/{id}")
    public ApiResponse<AiModel> detail(
            @PathVariable Long id
    ) {

        return ApiResponse.success(
                "查询模型详情成功",
                aiModelService.getById(id)
        );

    }



    //新增模型
    @PostMapping
    public ApiResponse<Void> add(
            @Valid
            @RequestBody AddModelRequest request
    ) {


        AiModel model = new AiModel();

        model.setModelCode(
                request.getModelCode()
        );

        model.setModelName(
                request.getModelName()
        );

        model.setProvider(
                request.getProvider()
        );

        model.setCategory(
                request.getCategory()
        );

        model.setCostPoints(
                request.getCostPoints()
        );

        model.setDescription(
                request.getDescription()
        );


        aiModelService.addModel(model);


        return ApiResponse.success(
                "新增模型成功",
                null
        );

    }



    //修改模型基础信息
    @PutMapping("/{id}")
    public ApiResponse<Void> update(
            @PathVariable Long id,

            @Valid
            @RequestBody UpdateModelRequest request
    ) {


        AiModel model = new AiModel();


        model.setId(id);

        model.setModelName(
                request.getModelName()
        );

        model.setProvider(
                request.getProvider()
        );

        model.setCategory(
                request.getCategory()
        );

        model.setDescription(
                request.getDescription()
        );


        aiModelService.updateModel(model);


        return ApiResponse.success(
                "修改模型成功",
                null
        );

    }



    //上架模型
    @PutMapping("/{id}/publish")
    public ApiResponse<Void> publish(
            @PathVariable Long id
    ) {


        aiModelService.publish(id);


        return ApiResponse.success(
                "模型上架成功",
                null
        );

    }



    //下架模型
    @PutMapping("/{id}/unpublish")
    public ApiResponse<Void> unpublish(
            @PathVariable Long id
    ) {


        aiModelService.unpublish(id);


        return ApiResponse.success(
                "模型下架成功",
                null
        );

    }



    //修改模型算力价格
    @PutMapping("/{id}/cost")
    public ApiResponse<Void> updateCost(
            @PathVariable Long id,

            @Valid
            @RequestBody UpdateCostPointsRequest request
    ) {


        aiModelService.updateCostPoints(
                id,
                request.getCostPoints()
        );


        return ApiResponse.success(
                "修改算力价格成功",
                null
        );

    }
}
