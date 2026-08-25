package com.fzujxl.aicomputerplatform.dto.task;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TaskOperateDto {
    private Long taskNo;
    private String prompt;
    private Long modelId;
    private Long userId;
}
