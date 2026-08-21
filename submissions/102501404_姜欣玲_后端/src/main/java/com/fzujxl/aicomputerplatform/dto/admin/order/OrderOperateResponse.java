package com.fzujxl.aicomputerplatform.dto.admin.order;

import com.fzujxl.aicomputerplatform.entity.Order;
import java.time.LocalDateTime;

public record OrderOperateResponse(
     String orderNo,
     Long userId,
     Long packageId,
     String packageName,
     String packageType,
     Long points,
     Long dailyPoints,
     Integer durationDays,
     Long amount,
     String status,
     LocalDateTime createdTime,
     LocalDateTime paymentTime
){
    public static OrderOperateResponse of(Order order) {
        return new OrderOperateResponse(
                order.getOrderNo(),
                order.getUserId(),
                order.getPackageId(),
                order.getPackageName(),
                order.getPackageType(),
                order.getPoints(),
                order.getDailyPoints(),
                order.getDuration(),
                order.getAmount(),
                order.getStatus(),
                order.getCreatedTime(),
                order.getPaymentTime()
        );
    }
}
