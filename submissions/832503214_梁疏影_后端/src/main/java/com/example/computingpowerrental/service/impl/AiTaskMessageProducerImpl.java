package com.example.computingpowerrental.service.impl;

import com.example.computingpowerrental.config.RabbitConfig;
import com.example.computingpowerrental.entity.AiTaskMessage;
import com.example.computingpowerrental.service.AiTaskGenerateService;
import com.example.computingpowerrental.service.AiTaskMessageConsumer;
import com.example.computingpowerrental.service.AiTaskMessageProducer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Lark
 * @ date 2026/8/7  16:48
 * @ description
 */
@Service
public class AiTaskMessageProducerImpl implements AiTaskMessageProducer{
    private final RabbitTemplate rabbitTemplate;

    public AiTaskMessageProducerImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void send(AiTaskMessage message){
        rabbitTemplate.convertAndSend(RabbitConfig.AI_TASK_QUEUE,message);
    }
}
