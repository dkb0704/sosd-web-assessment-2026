package com.aiplatform.service.creation;

import com.aiplatform.dto.creation.MyWorkDetailResponse;
import com.aiplatform.dto.creation.TaskDetailResponse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.multipart.MultipartFile;

public interface CreationService {
    Long submitTask(Long userId, Long modelId, String prompt, Boolean isPublic, MultipartFile[] files);
    TaskDetailResponse getTaskDetail(Long taskId);
    Page<TaskDetailResponse> getUserTasks(Long userId, int page, int size);
    void updateVisibility(Long taskId, Long userId, boolean isPublic);
    MyWorkDetailResponse getMyWorkDetail(Long workId, Long userId);
}