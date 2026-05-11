package com.example.aigenlease.service.impl;

import com.example.aigenlease.common.ApiResponse;

import com.example.aigenlease.dto.compute.response.*;

import com.example.aigenlease.service.ComputeProductService;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ComputeProductServiceImpl implements ComputeProductService {

    @Override
    public ApiResponse<List<ComputeProductResponse>> getProducts() {

        List<ComputeProductResponse> response = List.of(
                new ComputeProductResponse(
                        1L,
                        "100算力包",
                        100,
                        new BigDecimal("9.90"),
                        true
                ),
                new ComputeProductResponse(
                        2L,
                        "500算力包",
                        500,
                        new BigDecimal("39.90"),
                        true
                )
        );

        return ApiResponse.success("获取算力商品列表成功", response);
    }

    @Override
    public ApiResponse<ComputeProductDetailResponse> getProductDetail(
            Long productId
    ) {

        ComputeProductDetailResponse response =
                new ComputeProductDetailResponse(
                        productId,
                        "500算力包",
                        "适合高频AI生成用户使用",
                        500,
                        new BigDecimal("39.90"),
                        true
                );

        return ApiResponse.success("获取算力商品详情成功", response);
    }
}