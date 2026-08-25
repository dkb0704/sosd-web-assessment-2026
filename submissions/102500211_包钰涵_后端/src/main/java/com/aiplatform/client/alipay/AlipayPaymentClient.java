package com.aiplatform.client.alipay;

import com.alibaba.fastjson2.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.aiplatform.common.BusinessException;
import com.aiplatform.config.AlipayConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 支付宝支付客户端（封装常见操作）
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AlipayPaymentClient {

    private final AlipayClient alipayClient;
    private final AlipayConfig alipayConfig;

    /**
     * 生成电脑网站支付表单（返回完整HTML，前端可渲染或提取二维码）
     */
    public String pagePay(String orderNo, String subject, String amount) {
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(alipayConfig.getNotifyUrl());
        request.setReturnUrl(alipayConfig.getReturnUrl());

        Map<String, Object> bizContent = Map.of(
                "out_trade_no", orderNo,
                "total_amount", amount,
                "subject", subject,
                "product_code", "FAST_INSTANT_TRADE_PAY"
        );
        request.setBizContent(JSON.toJSONString(bizContent));

        try {
            AlipayTradePagePayResponse response = alipayClient.pageExecute(request);
            if (response.isSuccess()) {
                return response.getBody(); // HTML表单
            } else {
                log.error("支付宝下单失败：{}", response.getSubMsg());
                throw new BusinessException("创建支付订单失败：" + response.getSubMsg());
            }
        } catch (AlipayApiException e) {
            log.error("支付宝接口异常", e);
            throw new BusinessException("支付宝接口异常");
        }
    }

    /**
     * 查询订单支付状态
     */
    public boolean queryOrderPaySuccess(String orderNo) {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setBizContent("{\"out_trade_no\":\"" + orderNo + "\"}");
        try {
            AlipayTradeQueryResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                String tradeStatus = response.getTradeStatus();
                return "TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus);
            }
        } catch (AlipayApiException e) {
            log.error("支付宝查单异常", e);
        }
        return false;
    }

    /**
     * 获取支付宝交易号（从查询结果）
     */
    public String getTradeNo(String orderNo) {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setBizContent("{\"out_trade_no\":\"" + orderNo + "\"}");
        try {
            AlipayTradeQueryResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                return response.getTradeNo();
            }
        } catch (AlipayApiException e) {
            log.error("支付宝查单异常", e);
        }
        return null;
    }
}