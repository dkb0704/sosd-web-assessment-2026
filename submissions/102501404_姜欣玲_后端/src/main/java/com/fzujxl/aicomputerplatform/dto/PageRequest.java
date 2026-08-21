package com.fzujxl.aicomputerplatform.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PageRequest {
    @Min(1)
    @Max(100)
    private Integer pageSize = 20;

    @Min(1)
    private Integer pageNum = 1;
}
