package com.aiplatform.dto.creation;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class TaskSubmitRequest {
    private String prompt;
    private String modelId;
    private Boolean isPublic;
    // 附件由 Controller 的 @RequestParam 单独接收，不在此DTO内
}