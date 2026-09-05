package com.example.computingpowerrental.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * @author Lark
 * @ date 2026/8/16  13:17
 * @ description 创建充值订单请求DTO
 */
@Data
public class CreateRechargeOrderRequest {
    //充值套餐ID
    @NotNull(message = "充值套餐ID不能为空")
    @Positive(message = "充值套餐ID必须大于0")
    private Long packageId;
}
