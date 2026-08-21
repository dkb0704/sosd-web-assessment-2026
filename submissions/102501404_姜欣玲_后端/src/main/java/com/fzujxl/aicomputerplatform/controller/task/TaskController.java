package com.fzujxl.aicomputerplatform.controller.task;

import com.fzujxl.aicomputerplatform.common.Result;
import com.fzujxl.aicomputerplatform.dto.PageResultResponse;
import com.fzujxl.aicomputerplatform.dto.task.TaskQueryRequest;
import com.fzujxl.aicomputerplatform.dto.task.TaskSubmitRequest;
import com.fzujxl.aicomputerplatform.dto.task.TaskSubmitResponse;
import com.fzujxl.aicomputerplatform.entity.Task;
import com.fzujxl.aicomputerplatform.service.task.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task")
@Tag(name ="任务管理")
@Slf4j
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/query")
    @Operation(summary = "查询任务流水")
    public Result<PageResultResponse<Task>> queryTask(@Valid @RequestBody TaskQueryRequest request){
        log.info("查询任务流水:{}", request);
        PageResultResponse<Task> response = taskService.queryTask(request);
        return Result.success("查询成功", response);
    }

    @PostMapping("/submit")
    @Operation(summary = "提交任务")
    public Result<TaskSubmitResponse> submitTask(@RequestAttribute("userId") Long userId,
                                                 @Valid @RequestBody TaskSubmitRequest request){
        log.info("提交任务:{}", request);
        TaskSubmitResponse response = taskService.submitTask(userId,request);
        return Result.success("提交成功,任务排队中", response);
    }
}
