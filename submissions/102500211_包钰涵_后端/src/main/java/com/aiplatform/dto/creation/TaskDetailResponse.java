package com.aiplatform.dto.creation;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class TaskDetailResponse {
    private String taskId;
    private Integer status;
    private String result;
    private String errorMsg;
    private Integer pointsCost;
    private LocalDateTime submitTime;
    private LocalDateTime finishTime;
    // 新增字段
    private Boolean isPublic;
}