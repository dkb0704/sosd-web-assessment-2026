package com.example.aigenlease.controller;

import com.example.aigenlease.common.ApiResponse;

import com.example.aigenlease.dto.compute.response.*;

import com.example.aigenlease.service.ComputeProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/compute-products")
public class ComputeProductController {

    @Autowired
    private ComputeProductService computeProductService;

    @GetMapping
    public ApiResponse<List<ComputeProductResponse>> getProducts() {
        return computeProductService.getProducts();
    }

    @GetMapping("/{productId}")
    public ApiResponse<ComputeProductDetailResponse> getProductDetail(
            @PathVariable Long productId
    ) {
        return computeProductService.getProductDetail(productId);
    }
}