package com.aiplatform.controller.creation;

import com.aiplatform.common.Result;
import com.aiplatform.dto.creation.MyWorkDetailResponse;
import com.aiplatform.dto.creation.TaskDetailResponse;
import com.aiplatform.dto.creation.TaskSubmitRequest;
import com.aiplatform.service.creation.CreationService;
import com.aiplatform.mapper.AiModelMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j    // ← 加上这个注解，启用日志
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CreationController {

    private final CreationService creationService;
    private final AiModelMapper modelMapper;

    @GetMapping("/models")
    public Result<?> getModels() {
        return Result.success(modelMapper.selectList(null));
    }

    @PostMapping("/creation/task")
    public Result<?> submitTask(@RequestParam("prompt") String prompt,
                                @RequestParam("modelId") Long modelId,
                                @RequestParam(value = "isPublic", defaultValue = "true") Boolean isPublic,
                                @RequestParam(value = "files", required = false) MultipartFile[] files,
                                HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.unauthorized("请先登录");
        }
        Long taskId = creationService.submitTask(userId, modelId, prompt, isPublic, files);
        return Result.success("任务已提交，排队中", Map.of("taskId", String.valueOf(taskId)));
    }

    @GetMapping("/creation/task/{taskId}")
    public Result<?> getTask(@PathVariable Long taskId, HttpServletRequest request) {  // 加上 request 参数
        // 打印轮询请求到达日志
        log.error("!!! 轮询请求到达，taskId = {}", taskId);
        TaskDetailResponse detail = creationService.getTaskDetail(taskId);
        // 打印返回前日志
        log.error("!!! 轮询返回，taskId = {}, status = {}", taskId, detail.getStatus());
        return Result.success(detail);
    }

    @GetMapping("/creation/tasks")
    public Result<?> getMyTasks(@RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "20") int size,
                                HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("查询任务历史，userId = {}", userId);
        return Result.success(creationService.getUserTasks(userId, page, size));
    }

    @PutMapping("/creation/task/{taskId}/visibility")
    public Result<?> updateVisibility(@PathVariable Long taskId,
                                      @RequestBody Map<String, Boolean> body,
                                      HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        Boolean isPublic = body.get("isPublic");
        if (isPublic == null) {
            return Result.badRequest("缺少 isPublic 字段");
        }
        creationService.updateVisibility(taskId, userId, isPublic);
        return Result.success();
    }

    @GetMapping("/creation/works/{workId}")
    public Result<?> getMyWorkDetail(@PathVariable Long workId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("查询作品详情，workId = {}, userId = {}", workId, userId);
        MyWorkDetailResponse detail = creationService.getMyWorkDetail(workId, userId);
        return Result.success(detail);
    }
}