package com.fzujxl.aicomputerplatform.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long id;
    private String orderNo;
    private Long userId;
    private Long packageId;
    private String  packageType;
    private String packageName;
    private Long points;
    private Long dailyPoints;
    private Integer duration;
    private Long amount;
    private String status;
    private String tradeNo;
    private LocalDateTime paymentTime;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
