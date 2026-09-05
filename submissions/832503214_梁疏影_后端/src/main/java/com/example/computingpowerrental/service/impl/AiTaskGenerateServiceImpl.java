package com.example.computingpowerrental.service.impl;

import com.example.computingpowerrental.entity.AiTask;
import com.example.computingpowerrental.enums.AiTaskStatus;
import com.example.computingpowerrental.mapper.AiTaskMapper;
import com.example.computingpowerrental.service.AiTaskGenerateService;
import com.example.computingpowerrental.service.AiTaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Lark
 * @ date 2026/8/7  11:20
 * @ description AI任务生成服务实现类
 */
@Service
public class AiTaskGenerateServiceImpl implements AiTaskGenerateService{
    private final AiTaskMapper aiTaskMapper;

    public AiTaskGenerateServiceImpl(
            AiTaskMapper aiTaskMapper
    ) {
        this.aiTaskMapper = aiTaskMapper;
    }

    //模拟AI生成流程
    @Override
    public void mockGenerate(Long taskId) {

        boolean processingAcquired = false;

        try {
            //查询任务
            AiTask task = aiTaskMapper.findById(taskId);

            if (task == null) {
                return;
            }

            //检查当前状态
            if (!AiTaskStatus.WAITING.getCode().equals(task.getStatus())) {
                return;
            }

            //只有抢占 WAITING -> PROCESSING 成功的消费者才继续生成。
            int processingRows = aiTaskMapper.updateStatus(
                    taskId,
                    AiTaskStatus.WAITING.getCode(),
                    AiTaskStatus.PROCESSING.getCode()
            );

            if (processingRows == 0) {
                return;
            }

            processingAcquired = true;

            //模拟调用AI模型
            Thread.sleep(3000);

            String result = "这是AI生成的模拟内容：" + task.getPrompt();

            //PROCESSING -> SUCCESS
            aiTaskMapper.updateSuccess(
                    taskId,
                    AiTaskStatus.PROCESSING.getCode(),
                    AiTaskStatus.SUCCESS.getCode(),
                    result
            );

        } catch (Exception e) {

            e.printStackTrace();

            if (processingAcquired) {
                aiTaskMapper.updateFailed(
                        taskId,
                        AiTaskStatus.PROCESSING.getCode(),
                        AiTaskStatus.FAILED.getCode(),
                        e.getMessage()
                );
            }
        }
    }
}
