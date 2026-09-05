package com.example.computingpowerrental.enums;

/**
 * @author Lark
 * @ date 2026/8/15  12:15
 * @ description 充值订单状态
 */
public enum RechargeOrderStatus {
    PENDING(0, "待支付"),

    PAID(1, "已支付"),

    CLOSED(2, "已关闭");

    private final Integer code;

    private final String description;

    RechargeOrderStatus(
            Integer code,
            String description
    ) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
