package com.aiplatform.job;

import com.aiplatform.entity.RechargeOrder;
import com.aiplatform.mapper.RechargeOrderMapper;
import com.aiplatform.service.order.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderJobs {

    private final RechargeOrderMapper orderMapper;
    private final OrderService orderService;

    /**
     * 关闭超时未支付订单：每1分钟扫描一次
     */
    @Scheduled(fixedDelay = 60000)
    public void closeTimeoutOrders() {
        LocalDateTime thirtyMinutesAgo = LocalDateTime.now().minusMinutes(30);
        List<RechargeOrder> timeoutOrders = orderMapper.selectList(
                new LambdaQueryWrapper<RechargeOrder>()
                        .eq(RechargeOrder::getStatus, 0)
                        .lt(RechargeOrder::getCreateTime, thirtyMinutesAgo)
        );
        for (RechargeOrder order : timeoutOrders) {
            order.setStatus(2);
            orderMapper.updateById(order);
            log.info("超时关闭订单：{}", order.getOrderNo());
        }
    }

    /**
     * 掉单补偿：每天凌晨2点扫描创建超过10分钟且仍为待支付的订单，主动向支付宝查询
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void compensateOrders() {
        LocalDateTime tenMinAgo = LocalDateTime.now().minusMinutes(10);
        List<RechargeOrder> pendingOrders = orderMapper.selectList(
                new LambdaQueryWrapper<RechargeOrder>()
                        .eq(RechargeOrder::getStatus, 0)
                        .lt(RechargeOrder::getCreateTime, tenMinAgo)
        );
        for (RechargeOrder order : pendingOrders) {
            orderService.compensateOrder(order.getOrderNo());
        }
        log.info("掉单补偿扫描完成，处理订单数：{}", pendingOrders.size());
    }
}