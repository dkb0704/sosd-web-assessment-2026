package com.fzujxl.aicomputerplatform.controller.model;

import com.fzujxl.aicomputerplatform.common.Result;
import com.fzujxl.aicomputerplatform.dto.model.*;
import com.fzujxl.aicomputerplatform.service.model.ModelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/model")
@Slf4j
@Tag(name = "模型管理")
public class ModelController {

    private final ModelService modelService;

    public ModelController(ModelService modelService) {
        this.modelService = modelService;
    }

    @PostMapping("/add")
    @Operation(summary = "添加模型")
    public Result<AddModelResponse> addModel(@Valid @RequestBody AddModelRequest request){
        log.info("添加模型:{}", request);
        AddModelResponse response = modelService.addModel(request);
        return Result.success("添加成功", response);
    }

    @PutMapping("/publish")
    @Operation(summary = "上架模型")
    public Result<ChangeModelStatusResponse> publishModel(@Valid @RequestBody PublishModelRequest request){
        log.info("上架模型:{}", request);
        ChangeModelStatusResponse response = modelService.publishModel(request);
        return Result.success("上架成功", response);
    }

    @PutMapping("/offline")
    @Operation(summary = "下架模型")
    public Result<ChangeModelStatusResponse> offlineModel(@Valid @RequestBody OfflineModelRequest request){
        log.info("下架模型:{}", request);
        ChangeModelStatusResponse response = modelService.offlineModel(request);
        return Result.success("下架成功", response);
    }

    @PutMapping("/changeCostPoints")
    @Operation(summary = "修改模型成本积分")
    public Result<ChangeCostPointsResponse> changeCostPoints(@Valid @RequestBody ChangeCostPointsRequest request){
        log.info("修改模型成本积分:{}", request);
        ChangeCostPointsResponse response = modelService.changeCostPoints(request);
        return Result.success("修改成功", response);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除模型")
    public Result<?> deleteModel(@Valid @RequestBody DeleteModelRequest request){
        log.info("删除模型:{}", request);
        modelService.deleteModel(request);
        return Result.success("删除成功");
    }
}
