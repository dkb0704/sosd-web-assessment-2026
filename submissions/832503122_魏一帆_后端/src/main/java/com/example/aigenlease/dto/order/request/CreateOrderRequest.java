package com.example.aigenlease.dto.order.request;

import jakarta.validation.constraints.NotNull;

public record CreateOrderRequest(

        @NotNull(message = "商品ID不能为空")
        Long productId

) {
}