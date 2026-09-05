package com.example.computingpowerrental.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Lark
 * @ date 2026/8/7  17:07
 * @ description RabbitMQ配置类
 */
@Configuration
public class RabbitConfig {
    public static final String AI_TASK_QUEUE = "ai.task.queue";

    @Bean
    public Queue aiTaskQueue(){
        return new Queue(AI_TASK_QUEUE,true);
    }
}
