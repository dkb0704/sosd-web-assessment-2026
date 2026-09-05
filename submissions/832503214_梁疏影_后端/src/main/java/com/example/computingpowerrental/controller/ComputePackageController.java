package com.example.computingpowerrental.controller;

import com.example.computingpowerrental.dto.ApiResponse;
import com.example.computingpowerrental.entity.ComputePackage;
import com.example.computingpowerrental.service.ComputePackageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Lark
 * @ date 2026/8/15  18:12
 * @ description 用户端算力充值套餐接口
 */
@RestController
@RequestMapping("/api/packages")
public class ComputePackageController {
    private final ComputePackageService computePackageService;

    public ComputePackageController(ComputePackageService computePackageService) {
        this.computePackageService = computePackageService;
    }


    //查询当前可购买的充值套餐
    @GetMapping
    public ApiResponse<List<ComputePackage>> listAvailable() {
        return ApiResponse.success("查询充值套餐成功", computePackageService.listOnShelf());
    }
}
