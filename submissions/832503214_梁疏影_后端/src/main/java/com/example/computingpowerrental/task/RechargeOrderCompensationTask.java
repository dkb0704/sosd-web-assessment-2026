package com.example.computingpowerrental.task;

import com.example.computingpowerrental.entity.RechargeOrder;
import com.example.computingpowerrental.enums.RechargeOrderStatus;
import com.example.computingpowerrental.mapper.RechargeOrderMapper;
import com.example.computingpowerrental.service.AlipayService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Lark
 * @ date 2026/9/2  22:25
 * @ description 支付宝掉单补偿任务
 */
@Component
public class RechargeOrderCompensationTask {
    private final RechargeOrderMapper rechargeOrderMapper;

    private final AlipayService alipayService;

    public RechargeOrderCompensationTask(RechargeOrderMapper rechargeOrderMapper, AlipayService alipayService) {
        this.rechargeOrderMapper = rechargeOrderMapper;
        this.alipayService = alipayService;
    }

    /**
     * 每分钟主动查询已经到达过期时间、
     * 但本地仍然是PENDING的订单。
     */
    @Scheduled(fixedDelay = 60000)
    public void compensateOrders() {

        try {
            List<RechargeOrder> orders = rechargeOrderMapper.findTimeoutOrders(RechargeOrderStatus.PENDING.getCode(), LocalDateTime.now());
            //关单任务可能先一步将订单改为 CLOSED，仍需向支付宝查询以防止漏单。
            orders.addAll(
                    rechargeOrderMapper.findTimeoutOrders(
                            RechargeOrderStatus.CLOSED.getCode(),
                            LocalDateTime.now()
                    )
            );

            if (orders == null || orders.isEmpty()) {
                return;
            }

            int compensatedCount = 0;

            for (RechargeOrder order : orders) {

                boolean compensated = alipayService.queryAndCompensate(order.getOrderNo());

                if (compensated) {
                    compensatedCount++;
                }
            }

            if (compensatedCount > 0) {
                System.out.println("支付宝掉单补偿完成，本次补偿订单数：" + compensatedCount);
            }

        } catch (Exception e) {
            System.err.println("支付宝掉单补偿任务执行失败：" + e.getMessage());
        }
    }
}
