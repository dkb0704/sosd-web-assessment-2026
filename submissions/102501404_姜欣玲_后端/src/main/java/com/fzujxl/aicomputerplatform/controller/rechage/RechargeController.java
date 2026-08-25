package com.fzujxl.aicomputerplatform.controller.rechage;

import com.fzujxl.aicomputerplatform.common.Result;
import com.fzujxl.aicomputerplatform.dto.rechage.*;
import com.fzujxl.aicomputerplatform.service.recharge.RechargeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recharge")
@Slf4j
@Tag(name = "充值套餐管理")
public class RechargeController {

    public final RechargeService RechargeService;

    public RechargeController(RechargeService RechargeService) {
        this.RechargeService = RechargeService;
    }

    @PostMapping("/add")
    @Operation(summary = "添加充值套餐")
    public Result<AddRechargePackageResponse> addRechargePackage(@Valid @RequestBody AddRechargePackageRequest request){
        log.info("添加充值套餐:{}", request);
        AddRechargePackageResponse response = RechargeService.addRechargePackage(request);
        return Result.success("添加成功", response);
    }

    @PutMapping("/publish")
    @Operation(summary = "上架充值套餐")
    public Result<ChangeRechargePackageStatusResponse> publishRechargePackage(@Valid @RequestBody PublishRechargePackageRequest request){
        log.info("上架充值套餐:{}", request);
        ChangeRechargePackageStatusResponse response = RechargeService.publishRechargePackage(request);
        return Result.success("上架成功", response);
    }

    @PutMapping("/offline")
    @Operation(summary = "下架充值套餐")
    public Result<ChangeRechargePackageStatusResponse> offlineRechargePackage(@Valid @RequestBody OfflineRechargePackageRequest request){
        log.info("下架充值套餐:{}", request);
        ChangeRechargePackageStatusResponse response = RechargeService.offlineRechargePackage(request);
        return Result.success("下架成功", response);
    }
}
