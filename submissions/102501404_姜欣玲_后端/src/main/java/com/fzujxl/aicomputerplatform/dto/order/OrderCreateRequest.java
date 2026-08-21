package com.fzujxl.aicomputerplatform.dto.order;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderCreateRequest {
    @NotNull
    @NotEmpty
    private Long packageId;
}
