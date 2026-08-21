package com.fzujxl.aicomputerplatform.service.task;

import com.fzujxl.aicomputerplatform.dto.PageResultResponse;
import com.fzujxl.aicomputerplatform.dto.task.TaskQueryRequest;
import com.fzujxl.aicomputerplatform.dto.task.TaskSubmitRequest;
import com.fzujxl.aicomputerplatform.dto.task.TaskSubmitResponse;
import com.fzujxl.aicomputerplatform.entity.Task;

public interface TaskService {
    PageResultResponse<Task> queryTask(TaskQueryRequest request);

    TaskSubmitResponse submitTask(Long userId, TaskSubmitRequest request);
}
