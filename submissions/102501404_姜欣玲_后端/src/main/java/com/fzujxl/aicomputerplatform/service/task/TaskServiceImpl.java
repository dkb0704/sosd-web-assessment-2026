package com.fzujxl.aicomputerplatform.service.task;

import com.fzujxl.aicomputerplatform.common.ResultCode;
import com.fzujxl.aicomputerplatform.dto.PageResultResponse;
import com.fzujxl.aicomputerplatform.dto.task.TaskOperateDto;
import com.fzujxl.aicomputerplatform.dto.task.TaskQueryRequest;
import com.fzujxl.aicomputerplatform.dto.task.TaskSubmitRequest;
import com.fzujxl.aicomputerplatform.dto.task.TaskSubmitResponse;
import com.fzujxl.aicomputerplatform.entity.Task;
import com.fzujxl.aicomputerplatform.exception.BusinessException;
import com.fzujxl.aicomputerplatform.mapper.task.TaskMapper;
import com.fzujxl.aicomputerplatform.utils.SnowFlake;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskMapper taskMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final RabbitTemplate rabbitTemplate;

    public TaskServiceImpl(TaskMapper taskMapper, StringRedisTemplate stringRedisTemplate, RabbitTemplate rabbitTemplate) {
        this.taskMapper = taskMapper;
        this.stringRedisTemplate = stringRedisTemplate;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public PageResultResponse<Task> queryTask(TaskQueryRequest request) {
        log.info("查询任务流水申请:{}", request);
        PageHelper.startPage(request.getPageNum(), request.getPageSize());
        List<Task> taskList = taskMapper.selectByStatus(request.getStatus());
        PageInfo<Task> pageInfo = new PageInfo<>(taskList);
        return PageResultResponse.of(pageInfo);
    }

    @Override
    public TaskSubmitResponse submitTask(Long userId, TaskSubmitRequest request) {
        log.info("提交任务申请:{}", request);
        Long taskNo = SnowFlake.nextId();
        String taskKey = "task:" + taskNo;

            try {
                int result = taskMapper.submitTask(userId, taskNo, request);
                if (result<=0){
                    log.error("提交任务申请失败,userId={},taskNo={}",userId,taskNo);
                    throw new BusinessException(ResultCode.INTERNAL_ERROR,"提交任务申请失败,请稍后重试");
                }
            } catch (Exception e) {
                log.error("提交任务申请失败,userId={},taskNo={}",userId,taskNo,e);
                throw new BusinessException(ResultCode.INTERNAL_ERROR,"提交任务申请失败,请稍后重试");
            }

        stringRedisTemplate.opsForHash().putAll(taskKey, Map.of(
                "status", "PENDING",
                "userId", userId.toString(),
                "prompt", request.getPrompt(),
                "createTime", LocalDateTime.now().toString())
        );

        TaskOperateDto dto = new TaskOperateDto();
        dto.setTaskNo(taskNo);
        dto.setPrompt(request.getPrompt());
        dto.setModelId(request.getModelId());
        dto.setUserId(userId);
        String exchangeName = "task.exchange";
        rabbitTemplate.convertAndSend(exchangeName,"task.submit",dto);

        log.info("提交任务申请成功,userId={},taskNo={}",userId,taskNo);
        return new TaskSubmitResponse(taskNo);
    }
}
