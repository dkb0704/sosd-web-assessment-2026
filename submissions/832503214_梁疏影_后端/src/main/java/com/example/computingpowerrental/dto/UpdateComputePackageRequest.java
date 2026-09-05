package com.example.computingpowerrental.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Lark
 * @ date 2026/8/15  18:09
 * @ description 修改算力充值套餐请求DTO
 */
@Data
public class UpdateComputePackageRequest {
    //套餐名称
    @NotBlank(message = "套餐名称不能为空")
    @Size(max = 100, message = "套餐名称长度不能超过100个字符")
    private String packageName;

    //套餐包含算力点数
    @NotNull(message = "套餐算力不能为空")
    @Min(value = 1, message = "套餐算力必须大于0")
    private Integer points;

    //套餐价格
    @NotNull(message = "套餐价格不能为空")
    @DecimalMin(value = "0.01", message = "套餐价格必须大于0")
    private BigDecimal price;
}
