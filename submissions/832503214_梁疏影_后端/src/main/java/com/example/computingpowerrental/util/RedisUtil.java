package com.example.computingpowerrental.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author Lark
 * @ date 2026/7/15  11:35
 * @ description Redis工具类，对redis的常用操作进行统一封装
 */
@Component
public class RedisUtil {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 保存普通键值，不设置过期时间
     * @param key Redis Key
     * @param value Redis Value
     */
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 保存键值，并设置过期时间
     * @param key Redis Key
     * @param value Redis Value
     * @param timeout 有效时长
     * @param unit 时间单位
     */
    public void set(String key,
                    String value,
                    long timeout,
                    TimeUnit unit) {

        redisTemplate.opsForValue().set(
                key,
                value,
                timeout,
                unit
        );
    }

    /**
     * 根据 Key 获取 Value
     * @param key Redis Key
     * @return 对应的字符串值；Key 不存在时返回 null
     */
    public String get(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        return value == null ? null : value.toString();
    }

    /**
     * 判断某个 Key 是否存在
     * @param key Redis Key
     * @return true-存在，false-不存在
     */
    public boolean hasKey(String key) {
        Boolean result = redisTemplate.hasKey(key);
        return Boolean.TRUE.equals(result);
    }

    /**
     * 删除指定 Key
     * @param key Redis Key
     * @return true-删除成功，false-Key 不存在或删除失败
     */
    public boolean delete(String key) {
        Boolean result = redisTemplate.delete(key);
        return Boolean.TRUE.equals(result);
    }

    /**
     * 为已有 Key 设置过期时间
     * @param key Redis Key
     * @param timeout 有效时长
     * @param unit 时间单位
     * @return true-设置成功，false-Key 不存在或设置失败
     */
    public boolean expire(String key,
                          long timeout,
                          TimeUnit unit) {

        Boolean result = redisTemplate.expire(
                key,
                timeout,
                unit
        );

        return Boolean.TRUE.equals(result);
    }

    /**
     * 原子写入：只有 Key 不存在时才能写入成功
     * @param key Redis Key
     * @param value Redis Value
     * @param timeout 有效时长
     * @param unit 时间单位
     * @return true-写入成功；false-Key 已存在
     */
    public boolean setIfAbsent(String key,
                               String value,
                               long timeout,
                               TimeUnit unit) {

        Boolean result = redisTemplate.opsForValue()
                .setIfAbsent(
                        key,
                        value,
                        timeout,
                        unit
                );

        return Boolean.TRUE.equals(result);
    }

    /**
     * 原子扣减数值。
     *
     * 返回值：
     *  1  扣减成功
     * -1  余额不足
     * -2  Key不存在
     */
    public Long atomicDecrease(String key, Integer points) {

        if (key == null || points == null || points <= 0) {
            throw new IllegalArgumentException("Redis Key不能为空，扣减值必须大于0");
        }

        String script =
                "local current = redis.call('GET', KEYS[1]); " +
                        "if not current then " +
                        "    return -2; " +
                        "end; " +
                        "current = tonumber(current); " +
                        "local cost = tonumber(ARGV[1]); " +
                        "if current < cost then " +
                        "    return -1; " +
                        "end; " +
                        "redis.call('DECRBY', KEYS[1], cost); " +
                        "return 1;";

        DefaultRedisScript<Long> redisScript =
                new DefaultRedisScript<>(script, Long.class);

        return redisTemplate.execute(
                redisScript,
                Collections.singletonList(key),
                points.toString()
        );
    }

    /**
     * 原子增加数值。
     * 主要用于Redis算力扣减后的异常补偿。
     */
    public Long atomicIncrease(String key, Integer points) {

        if (key == null || points == null || points <= 0) {
            throw new IllegalArgumentException("Redis Key不能为空，增加值必须大于0");
        }

        return redisTemplate.opsForValue().increment(key, points);
    }
}
