package com.example.computingpowerrental.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author Lark
 * @ date 2026/7/15  10:52
 * @ description Redis配置类（创建RedisTemplate、配置Redis数据序列化方式。后续业务层通过RedisTemplate操作Redis）
 */
@Configuration
public class RedisConfig {
    /**
     * 创建RedisTemplate Bean
     * @param factory Redis连接工厂
     * @return RedisTemplate
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory factory) {

        RedisTemplate<String, Object> template =
                new RedisTemplate<>();

        // 设置Redis连接
        template.setConnectionFactory(factory);

        //设置Key序列化方式
        template.setKeySerializer(
                new StringRedisSerializer()
        );

        //设置Value序列化方式
        template.setValueSerializer(
                new StringRedisSerializer()
        );

        // 初始化配置
        template.afterPropertiesSet();

        return template;
    }
}
