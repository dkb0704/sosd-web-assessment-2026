package com.aiplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 充值订单表
 */
@Data
@TableName("recharge_order")
public class RechargeOrder {

    /** 订单号（程序生成，如 20260428123456789） */
    @TableId(type = IdType.INPUT)
    private String orderNo;

    /** 用户 ID */
    private Long userId;

    /** 套餐 ID */
    private Long productId;

    /** 支付金额 */
    private BigDecimal amount;

    /** 购买获得的算力点数 */
    private Integer pointsGained;

    /** 订单状态：0-待支付 1-已支付 2-已关闭 */
    private Integer status;

    /** 支付宝交易号 */
    private String alipayTradeNo;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 支付时间 */
    private LocalDateTime payTime;
}