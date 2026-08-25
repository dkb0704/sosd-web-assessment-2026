package com.aiplatform.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 配置
 */
@Configuration
public class RabbitConfig {

    /** AI 生成任务队列名 */
    public static final String TASK_QUEUE = "ai.task.queue";

    /** 声明队列（持久化） */
    @Bean
    public Queue taskQueue() {
        return new Queue(TASK_QUEUE, true);
    }

    /** JSON 消息转换器（替代默认的 SimpleMessageConverter） */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}