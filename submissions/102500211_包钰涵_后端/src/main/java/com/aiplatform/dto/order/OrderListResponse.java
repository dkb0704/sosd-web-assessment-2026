package com.aiplatform.dto.order;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderListResponse {
    private String orderNo;
    private BigDecimal amount;
    private Integer pointsGained;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime payTime;
}