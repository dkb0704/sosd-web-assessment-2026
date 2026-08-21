package com.fzujxl.aicomputerplatform.dto.model;

import com.fzujxl.aicomputerplatform.validation.PermitNullButNotEmpty;
import com.fzujxl.aicomputerplatform.validation.ValidWorkType;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddModelRequest {

    @NotBlank
    @Size(min = 1, max = 20)
    private String modelKey;

    @NotBlank
    @ValidWorkType
    private String modelType;

    @NotNull
    @NotEmpty
    @Min(1)
    private Long costPoints;

    @Size(max = 300)
    private String description;

    @NotNull
    @NotEmpty
    @Min(0)
    @Max(1)
    private Integer status;

    @PermitNullButNotEmpty
    @Min(0)
    private Integer sortOrder;

}
