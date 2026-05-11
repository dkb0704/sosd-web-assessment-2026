package com.example.aigenlease.service;

import com.example.aigenlease.common.ApiResponse;

import com.example.aigenlease.dto.compute.response.*;

import java.util.List;

public interface ComputeProductService {

    ApiResponse<List<ComputeProductResponse>> getProducts();
    ApiResponse<ComputeProductDetailResponse> getProductDetail(Long productId);
}