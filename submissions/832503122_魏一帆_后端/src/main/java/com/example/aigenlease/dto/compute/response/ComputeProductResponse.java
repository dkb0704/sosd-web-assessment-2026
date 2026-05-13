package com.example.aigenlease.dto.compute.response;

import java.math.BigDecimal;

public record ComputeProductResponse(
        Long productId,
        String productName,
        Integer computePoints,
        BigDecimal price,
        Boolean published
) {
}