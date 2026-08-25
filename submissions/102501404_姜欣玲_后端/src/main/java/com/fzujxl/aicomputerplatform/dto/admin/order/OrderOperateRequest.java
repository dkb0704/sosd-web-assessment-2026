package com.fzujxl.aicomputerplatform.dto.admin.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderOperateRequest {

    @NotNull
    @NotEmpty
    private Long userId;

    @NotNull
    @NotEmpty
    private Long packageId;

    @NotBlank
    private String tradeNo;

    @NotBlank
    private String status;
}
