package com.fzujxl.aicomputerplatform.dto.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChangeCostPointsRequest {
    @NotBlank
    @Size(min = 1,max = 20)
    private String modelKey;

    @NotNull
    @NotEmpty
    private Long costPoints;
}
