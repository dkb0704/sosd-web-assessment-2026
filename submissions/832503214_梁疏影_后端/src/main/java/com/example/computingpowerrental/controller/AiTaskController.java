package com.example.computingpowerrental.controller;

import com.example.computingpowerrental.dto.ApiResponse;
import com.example.computingpowerrental.dto.SubmitTaskRequest;
import com.example.computingpowerrental.dto.TaskQueryRequest;
import com.example.computingpowerrental.entity.AiTask;
import com.example.computingpowerrental.service.AiTaskService;
import com.example.computingpowerrental.util.RedisUtil;
import com.example.computingpowerrental.util.RequestHolder;
import com.example.computingpowerrental.vo.TaskPageVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Lark
 * @ date 2026/8/6  18:26
 * @ description AI任务用户端接口
 */
@RestController
@RequestMapping("/api/tasks")
public class AiTaskController {

    private final AiTaskService aiTaskService;

    private final RedisUtil redisUtil;

    public AiTaskController(AiTaskService aiTaskService,RedisUtil redisUtil) {
        this.aiTaskService = aiTaskService;
        this.redisUtil = redisUtil;
    }

    //提交AI生成任务
    @PostMapping
    public ApiResponse<AiTask> submit(HttpServletRequest httpRequest,@Valid@RequestBody SubmitTaskRequest request) {
        String ip = httpRequest.getRemoteAddr();
        String limitKey = "ai_task_limit:" + ip;

        boolean allowed = redisUtil.setIfAbsent(limitKey,"1",10,TimeUnit.SECONDS);

        if(!allowed){
            return ApiResponse.error(400,"操作过于频繁，请稍后再试");
        }

        Long userId = RequestHolder.getUserId();

        AiTask task = aiTaskService.submitTask(userId,request.getPrompt(),request.getModelId(),request.getCategory(),request.getIsPublic());

        return ApiResponse.success("提交AI任务成功",task);
    }

    //查询任务详情
    @GetMapping("/{id}")
    public ApiResponse<AiTask> detail(@PathVariable Long id) {
        Long userId = RequestHolder.getUserId();

        return ApiResponse.success("查询任务成功",aiTaskService.getTaskById(id,userId)
        );
    }

    //查询任务
    @GetMapping("/my")
    public ApiResponse<TaskPageVO> myTasks(TaskQueryRequest request){

        Long userId = RequestHolder.getUserId();


        return ApiResponse.success(
                "查询任务列表成功",
                aiTaskService.pageTasks(
                        userId,
                        request.getPage(),
                        request.getSize(),
                        request.getStatus()
                )
        );
    }

    @GetMapping("/gallery")
    public ApiResponse<TaskPageVO> gallery(
            @RequestParam(required = false)String category,
            @RequestParam(required = false)String sort,
            @RequestParam(defaultValue = "1")Integer page,
            @RequestParam(defaultValue = "10")Integer size
    ){
        return ApiResponse.success("查询作品广场成功",aiTaskService.gallery(
                category,
                sort,
                page,
                size
                )
        );
    }

    @PutMapping("/{id}/public")
    public ApiResponse<Void> updatePublicStatus(@PathVariable Long id,@RequestParam Boolean isPublic) {

        Long userId = RequestHolder.getUserId();

        aiTaskService.updatePublicStatus(id,userId,isPublic);

        return ApiResponse.success(
                isPublic
                        ? "作品公开成功"
                        : "作品取消公开成功",
                null
        );
    }

    @PostMapping("/{id}/like")
    public ApiResponse<Void> likeTask(@PathVariable Long id) {

        aiTaskService.likeTask(id);

        return ApiResponse.success(
                "点赞成功",
                null
        );
    }
}
