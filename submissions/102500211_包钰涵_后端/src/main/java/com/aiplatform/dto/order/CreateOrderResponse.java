package com.aiplatform.dto.order;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateOrderResponse {
    private String orderNo;
    private String qrCode;  // 或支付链接/表单
}