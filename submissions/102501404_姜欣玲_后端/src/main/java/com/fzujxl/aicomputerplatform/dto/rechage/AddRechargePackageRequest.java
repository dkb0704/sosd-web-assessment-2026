package com.fzujxl.aicomputerplatform.dto.rechage;

import com.fzujxl.aicomputerplatform.validation.PermitNullButNotEmpty;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddRechargePackageRequest {

    @NotBlank
    @Size(min = 1,max = 20)
    private String packageName;

    @NotBlank
    @Size(min = 1,max = 20)
    private String packageType;

    @NotBlank
    @Size(min = 1,max = 300)
    private String packageDescription;

    @NotNull
    @Min(1)
    private Long points;

    @NotNull
    @Min(1)
    private Long dailyPoints;

    @NotNull
    @Min(0)
    private Long originalPrice;

    @NotNull
    @Min(0)
    private Long currentPrice;

    @NotNull
    @Size
    private Integer duration;

    @NotNull
    @Min(0)
    @Max(1)
    private Integer status;

    @PermitNullButNotEmpty
    @Min(0)
    private Integer sortOrder;
}
