package com.fzujxl.aicomputerplatform.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Task {
    private Long id;
    private Long taskNo;
    private Long userId;
    private Long modelId;
    private String prompt;
    private String status;
    private Long costPoints;
    private LocalDateTime createdTime;
    private LocalDateTime startedTime;
    private LocalDateTime finishedTime;
    private Integer deleted;
    private String resultUrl;
    private String errorMsg;
}
