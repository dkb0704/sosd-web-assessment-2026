package com.aiplatform.dto.creation;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class MyWorkDetailResponse {
    private String taskId;
    private Integer status;
    private String result;
    private String errorMsg;
    private Integer pointsCost;
    private Boolean isPublic;
    private Integer likeCount;
    private Integer commentCount;
    private LocalDateTime submitTime;
    private LocalDateTime finishTime;
    private String prompt;
}