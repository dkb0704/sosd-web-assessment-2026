package com.example.computingpowerrental.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author Lark
 * @ date 2026/8/7  16:07
 * @ description
 */
@Configuration
public class AsyncConfig {
    @Bean("aiTaskExecutor")
    public Executor aiTaskExecutor(){

        ThreadPoolTaskExecutor executor =
                new ThreadPoolTaskExecutor();

        //核心线程数量
        executor.setCorePoolSize(5);

        //最大线程数量
        executor.setMaxPoolSize(10);

        //任务队列大小
        executor.setQueueCapacity(100);

        //线程名前缀
        executor.setThreadNamePrefix(
                "ai-task-"
        );

        //初始化
        executor.initialize();

        return executor;
    }
}
