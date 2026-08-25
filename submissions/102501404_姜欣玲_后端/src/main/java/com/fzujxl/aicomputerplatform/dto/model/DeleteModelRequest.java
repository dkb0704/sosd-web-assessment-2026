package com.fzujxl.aicomputerplatform.dto.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeleteModelRequest {
    @NotBlank
    @Size(min = 1, max = 20)
    private String modelKey;

}
