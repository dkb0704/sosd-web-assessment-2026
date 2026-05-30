package com.aiplatform.dto.admin;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PointsModifyRequest {
    @NotNull
    private String userId;
    @NotNull
    private Integer pointsChange;  // 正数增加，负数扣除
}