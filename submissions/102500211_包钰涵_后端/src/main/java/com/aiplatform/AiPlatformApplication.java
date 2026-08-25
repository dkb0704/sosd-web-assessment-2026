package com.aiplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * AI 创作与算力租赁平台启动类
 */
@SpringBootApplication
@EnableScheduling   // 启用定时任务（订单补偿、超时关单等）
@EnableAsync        // 启用异步（邮件发送）
public class AiPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiPlatformApplication.class, args);
    }
}