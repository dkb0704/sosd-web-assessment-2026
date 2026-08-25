package com.fzujxl.aicomputerplatform.dto.rechage;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OfflineRechargePackageRequest {
    @NotBlank
    @Size(min = 1,max = 20)
    private String packageName;

    @NotNull
    @NotEmpty
    @Min(0)
    @Max(1)
    private Integer status;
}
