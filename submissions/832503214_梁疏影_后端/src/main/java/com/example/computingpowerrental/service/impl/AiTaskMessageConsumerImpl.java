package com.example.computingpowerrental.service.impl;

import com.example.computingpowerrental.entity.AiTaskMessage;
import com.example.computingpowerrental.service.AiTaskGenerateService;
import com.example.computingpowerrental.service.AiTaskMessageConsumer;
import org.springframework.stereotype.Service;
import com.example.computingpowerrental.config.RabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * @author Lark
 * @ date 2026/8/7  16:57
 * @ description
 */
@Service
public class AiTaskMessageConsumerImpl implements AiTaskMessageConsumer{
    private final AiTaskGenerateService aiTaskGenerateService;

    public AiTaskMessageConsumerImpl(AiTaskGenerateService aiTaskGenerateService){
        this.aiTaskGenerateService = aiTaskGenerateService;
    }

    @Override
    @RabbitListener(queues = RabbitConfig.AI_TASK_QUEUE)
    public void consume(AiTaskMessage message){
        aiTaskGenerateService.mockGenerate(
                message.getTaskId()
        );
    }
}
