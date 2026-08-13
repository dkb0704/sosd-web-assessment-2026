package com.example.computingpowerrental.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Lark
 * @ date 2026/7/27  17:16
 * @ description 修改模型算力价格请求DTO
 */
@Data
public class UpdateCostPointsRequest {
    //每次调用消耗算力点数
    @NotNull(message = "算力价格不能为空")
    @Min(value = 0,
            message = "算力价格不能小于0")
    private Integer costPoints;
}
