package com.example.computingpowerrental.task;

import com.example.computingpowerrental.service.RechargeOrderService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Lark
 * @ date 2026/8/26  19:30
 * @ description 充值订单超时关闭定时任务
 */
@Component
public class RechargeOrderTimeoutTask {
    private final RechargeOrderService rechargeOrderService;

    public RechargeOrderTimeoutTask(RechargeOrderService rechargeOrderService) {
        this.rechargeOrderService = rechargeOrderService;
    }

    /**
     * 每分钟扫描一次超时未支付订单
     */
    @Scheduled(initialDelay = 30000, fixedDelay = 60000)
    public void closeTimeoutOrders() {
        try {
            int closedCount = rechargeOrderService.closeTimeoutOrders();

            if (closedCount > 0) {
                System.out.println("充值订单超时关闭完成，本次关闭订单数：" + closedCount);
            }

        } catch (Exception e) {
            System.err.println("充值订单超时关闭任务执行失败：" + e.getMessage());
        }
    }
}
