package com.example.computingpowerrental.service;

import com.example.computingpowerrental.entity.AiTask;
import com.example.computingpowerrental.vo.TaskPageVO;

import java.util.List;

/**
 * @author Lark
 * @ date 2026/8/6  18:07
 * @ description AI任务业务接口（提交AI生成任务、查询任务、更新任务状态）
 */
public interface AiTaskService {
    //提交AI生成任务
    AiTask submitTask(Long userId, String prompt, Long modelId, String category, Boolean isPublic);

    //根据任务ID查询任务
    AiTask getTaskById(Long id, Long userId);

    //查询用户任务列表
    List<AiTask> listUserTasks(Long userId);

    TaskPageVO pageTasks(Long userId, Integer page, Integer size, Integer status);

    //管理端分页查询全站AI任务
    TaskPageVO adminPageTasks(Integer page, Integer size, Integer status);

    //查询公开作品
    TaskPageVO gallery(String category, String sort, Integer page, Integer size);

    //更新任务成功状态
    void success(Long taskId, String result);

    //更新任务失败状态
    void failed(Long taskId, String errorMessage);

    //修改任务公开状态
    void updatePublicStatus(Long taskId, Long userId, Boolean isPublic);

    void likeTask(Long taskId);
}
