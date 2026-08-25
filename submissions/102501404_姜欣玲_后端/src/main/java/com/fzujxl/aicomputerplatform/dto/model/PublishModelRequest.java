package com.fzujxl.aicomputerplatform.dto.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PublishModelRequest {
    @NotBlank
    @Size(min = 1,max = 20)
    private String modelKey;

    @NotNull
    @NotEmpty
    @Min(0)
    @Max(1)
    private Integer status;
}
