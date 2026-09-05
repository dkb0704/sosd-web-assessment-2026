package com.example.computingpowerrental.controller;

import com.example.computingpowerrental.dto.AddComputePackageRequest;
import com.example.computingpowerrental.dto.ApiResponse;
import com.example.computingpowerrental.dto.UpdateComputePackageRequest;
import com.example.computingpowerrental.entity.ComputePackage;
import com.example.computingpowerrental.service.ComputePackageService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Lark
 * @ date 2026/8/16  12:23
 * @ description 管理端算力充值套餐接口
 */
@RestController
@RequestMapping("/api/admin/packages")
public class AdminComputePackageController {
    private final ComputePackageService computePackageService;

    public AdminComputePackageController(ComputePackageService computePackageService) {
        this.computePackageService = computePackageService;
    }

    //查询全部套餐
    @GetMapping
    public ApiResponse<List<ComputePackage>> list() {
        return ApiResponse.success("查询充值套餐列表成功", computePackageService.listAll());
    }

    //查询套餐详情
    @GetMapping("/{id}")
    public ApiResponse<ComputePackage> detail(@PathVariable Long id) {
        return ApiResponse.success("查询充值套餐详情成功", computePackageService.getById(id));
    }

    //新增套餐
    @PostMapping
    public ApiResponse<Void> add(@Valid @RequestBody AddComputePackageRequest request) {

        ComputePackage computePackage = new ComputePackage();

        computePackage.setPackageName(request.getPackageName());

        computePackage.setPoints(request.getPoints());

        computePackage.setPrice(request.getPrice());

        computePackageService.addPackage(computePackage);

        return ApiResponse.success("新增充值套餐成功", null);
    }

    //修改套餐
    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable Long id, @Valid @RequestBody UpdateComputePackageRequest request) {

        ComputePackage computePackage = new ComputePackage();

        computePackage.setId(id);

        computePackage.setPackageName(request.getPackageName());

        computePackage.setPoints(request.getPoints());

        computePackage.setPrice(request.getPrice());

        computePackageService.updatePackage(computePackage);

        return ApiResponse.success("修改充值套餐成功", null);
    }

    //上架套餐
    @PutMapping("/{id}/publish")
    public ApiResponse<Void> publish(@PathVariable Long id) {

        computePackageService.publish(id);

        return ApiResponse.success("充值套餐上架成功", null);
    }

    //下架套餐
    @PutMapping("/{id}/unpublish")
    public ApiResponse<Void> unpublish(@PathVariable Long id) {

        computePackageService.unpublish(id);

        return ApiResponse.success("充值套餐下架成功", null);
    }
}
