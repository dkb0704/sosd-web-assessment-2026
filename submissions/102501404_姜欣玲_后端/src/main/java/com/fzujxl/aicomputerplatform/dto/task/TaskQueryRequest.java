package com.fzujxl.aicomputerplatform.dto.task;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TaskQueryRequest {
    @NotEmpty
    @NotNull
    private Integer status;

    @Min(1)
    @Max(100)
    private Integer pageSize = 20;

    @Min(1)
    @Max(100)
    private Integer pageNum = 1;
}
