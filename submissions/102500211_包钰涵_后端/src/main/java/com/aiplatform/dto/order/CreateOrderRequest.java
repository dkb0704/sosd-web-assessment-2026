package com.aiplatform.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOrderRequest {
    @NotNull(message = "套餐ID不能为空")
    private Long productId;
}