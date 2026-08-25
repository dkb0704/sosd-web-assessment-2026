package com.aiplatform.consumer;

import com.aiplatform.client.aimodel.AIServiceClient;
import com.aiplatform.client.aimodel.MockAIServiceClient;
import com.aiplatform.config.RabbitConfig;
import com.aiplatform.entity.AiModel;
import com.aiplatform.entity.CreationTask;
import com.aiplatform.mapper.AiModelMapper;
import com.aiplatform.mapper.CreationTaskMapper;
import com.aiplatform.mapper.UserMapper;
import com.aiplatform.service.ai.AIService;
import com.aiplatform.util.MailUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskConsumer {

    private final CreationTaskMapper taskMapper;
    private final AiModelMapper modelMapper;
    private final UserMapper userMapper;
    private final AIServiceClient aiServiceClient;      // 处理图像（SpringAiClient）
    private final MockAIServiceClient mockAIServiceClient; // 兜底 Mock
    private final AIService aiService;                  // 处理文本（Ollama）
    private final MailUtil mailUtil;

    @RabbitListener(queues = RabbitConfig.TASK_QUEUE)
    @Transactional
    public void handleTask(Long taskId) {
        log.error("!!! MQ消费者收到任务，taskId={}", taskId);
        CreationTask task = taskMapper.selectById(taskId);
        if (task == null || task.getStatus() != 0) {
            log.warn("任务不存在或状态非排队中：{}", taskId);
            return;
        }

        AiModel model = modelMapper.selectById(task.getModelId());
        if (model == null) {
            failTask(task, "模型不存在");
            return;
        }

        try {
            String result;
            // 根据模型类型选择不同的调用方式
            if ("text".equals(model.getModelKey())) {
                // 文本生成：使用 AIService（Ollama），失败时降级
                try {
                    result = aiService.generateText(task.getPrompt());
                    log.info("Ollama 文本生成成功：{}", taskId);
                } catch (Exception e) {
                    log.error("Ollama 调用失败，降级到 Mock：{}", e.getMessage());
                    result = mockAIServiceClient.generate(model, task.getPrompt(), task.getFileUrls());
                }
            } else if ("image".equals(model.getModelKey())) {
                // 图像生成：使用 AIServiceClient（SpringAiClient → Pollinations），失败时降级
                try {
                    result = aiServiceClient.generate(model, task.getPrompt(), task.getFileUrls());
                    log.info("图像生成成功：{}", taskId);
                } catch (Exception e) {
                    log.error("图像生成失败，降级到 Mock：{}", e.getMessage());
                    result = mockAIServiceClient.generate(model, task.getPrompt(), task.getFileUrls());
                }
            } else {
                throw new IllegalArgumentException("不支持的模型类型: " + model.getModelKey());
            }

            task.setResult(result);
            task.setStatus(1);
            task.setFinishTime(LocalDateTime.now());
            taskMapper.updateById(task);
            log.info("任务处理成功：{}", taskId);
        } catch (Exception e) {
            log.error("任务处理失败（Mock 也失败）：{}", taskId, e);
            failTask(task, e.getMessage());
        }
    }

    private void failTask(CreationTask task, String errorMsg) {
        task.setStatus(2);
        task.setErrorMsg(errorMsg);
        task.setFinishTime(LocalDateTime.now());
        taskMapper.updateById(task);
    }
}