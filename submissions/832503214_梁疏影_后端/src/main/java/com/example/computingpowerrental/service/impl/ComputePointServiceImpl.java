package com.example.computingpowerrental.service.impl;

import com.example.computingpowerrental.entity.User;
import com.example.computingpowerrental.mapper.UserMapper;
import com.example.computingpowerrental.service.ComputePointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.computingpowerrental.util.RedisUtil;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.concurrent.TimeUnit;

/**
 * @author Lark
 * @ date 2026/8/7  11:30
 * @ description
 */
@Service
public class ComputePointServiceImpl implements ComputePointService{
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    @Transactional
    public boolean consumePoints(Long userId, Integer points) {

        if (userId == null) {
            throw new RuntimeException("用户ID不能为空");
        }

        if (points == null || points <= 0) {
            throw new RuntimeException("扣减的算力点必须大于0");
        }

        String redisKey = "user:compute_points:" + userId;

        //先尝试直接从Redis原子扣减
        Long result = redisUtil.atomicDecrease(redisKey, points);

        //Redis中还没有该用户的算力缓存
        if (result != null && result == -2L) {

            User user = userMapper.findById(userId);

            if (user == null) {
                throw new RuntimeException("用户不存在");
            }

            Integer currentPoints = user.getComputePoints();

            if (currentPoints == null) {
                currentPoints = 0;
            }

            /*
             * 只在Key不存在时初始化。
             * 多个请求同时初始化时，只有一个请求能够成功写入，
             * 防止旧余额覆盖其他请求已经扣减后的余额。
             */
            redisUtil.setIfAbsent(
                    redisKey,
                    currentPoints.toString(),
                    1,
                    TimeUnit.DAYS
            );

            //初始化后重新执行一次原子扣减
            result = redisUtil.atomicDecrease(redisKey, points);
        }

        //Redis判断算力不足
        if (result == null || result == -1L) {
            return false;
        }

        if (result != 1L) {
            throw new RuntimeException("Redis算力扣减失败");
        }

        /*
         * Redis扣减成功后，同步扣减MySQL中的持久化余额。
         */
        int rows = userMapper.decreaseComputePoints(userId, points);

        if (rows == 0) {
            redisUtil.atomicIncrease(redisKey, points);
            throw new RuntimeException("算力数据同步失败");
        }

        /*
         * 如果外层数据库事务最终回滚，
         * 则把Redis已经扣除的算力补回来。
         */
        if (TransactionSynchronizationManager.isSynchronizationActive()) {

            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCompletion(int status) {

                            if (status == TransactionSynchronization.STATUS_ROLLED_BACK) {
                                redisUtil.atomicIncrease(redisKey, points);
                            }
                        }
                    }
            );
        }
        return true;
    }

    @Override
    @Transactional
    public void addPoints(Long userId, Integer points) {

        if (userId == null) {
            throw new RuntimeException("用户ID不能为空");
        }

        if (points == null || points <= 0) {
            throw new RuntimeException("增加的算力点必须大于0");
        }

        User user = userMapper.findById(userId);

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        int rows = userMapper.increaseComputePoints(userId, points);

        if (rows == 0) {
            throw new RuntimeException("增加用户算力失败");
        }

        String redisKey = "user:compute_points:" + userId;

        /*
         * 必须等MySQL事务真正提交成功以后再删除Redis缓存。
         * 下一次扣减时会重新从MySQL加载最新余额。
         */
        if (TransactionSynchronizationManager.isSynchronizationActive()) {

            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            redisUtil.delete(redisKey);
                        }
                    }
            );

        } else {
            redisUtil.delete(redisKey);
        }
    }

    @Override
    public Integer getPoints(Long userId){

        User user=userMapper.findById(userId);

        if(user==null){
            throw new RuntimeException("用户不存在");
        }

        return user.getComputePoints();
    }
}
