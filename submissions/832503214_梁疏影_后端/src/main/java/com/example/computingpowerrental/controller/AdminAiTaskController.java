package com.example.computingpowerrental.controller;

import com.example.computingpowerrental.dto.ApiResponse;
import com.example.computingpowerrental.service.AiTaskService;
import com.example.computingpowerrental.vo.TaskPageVO;
import org.springframework.web.bind.annotation.*;

/**
 * @author Lark
 * @ date 2026/9/2  23:17
 * @ description 管理端AI任务流水接口
 */
@RestController
@RequestMapping("/api/admin/tasks")
public class AdminAiTaskController {
    private final AiTaskService aiTaskService;

    public AdminAiTaskController(AiTaskService aiTaskService) {
        this.aiTaskService = aiTaskService;
    }


    //分页查询全站AI任务流水
    @GetMapping
    public ApiResponse<TaskPageVO> pageTasks(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestParam(required = false) Integer status) {

        return ApiResponse.success("查询AI任务流水成功", aiTaskService.adminPageTasks(page, size, status));
    }
}
