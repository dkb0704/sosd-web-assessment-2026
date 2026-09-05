package com.example.computingpowerrental.service;

/**
 * @author Lark
 * @ date 2026/8/7  11:29
 * @ description 扣减算力点
 */
public interface ComputePointService {
    //扣减算力点
    boolean consumePoints(Long userId, Integer points);

    //增加算力点
    void addPoints(Long userId, Integer points);

    //查询用户算力点
    Integer getPoints(Long userId);
}
