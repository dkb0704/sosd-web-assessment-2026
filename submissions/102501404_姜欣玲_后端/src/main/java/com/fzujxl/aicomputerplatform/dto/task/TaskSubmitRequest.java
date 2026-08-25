package com.fzujxl.aicomputerplatform.dto.task;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TaskSubmitRequest {
    @NotBlank
    private String prompt;

    @NotBlank
    private Long modelId;
}
