package com.example.aigenlease.dto.compute.response;

import java.math.BigDecimal;

public record ComputeProductDetailResponse(
        Long productId,
        String productName,
        String description,
        Integer computePoints,
        BigDecimal price,
        Boolean published
) {
}