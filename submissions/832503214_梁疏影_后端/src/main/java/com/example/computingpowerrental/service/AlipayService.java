package com.example.computingpowerrental.service;

import java.util.Map;

/**
 * @author Lark
 * @ date 2026/8/26  12:09
 * @ description 支付宝支付业务接口
 */
public interface AlipayService {
    //生成电脑网站支付页面
    String createPagePay(Long userId, Long orderId);

    //处理支付宝异步通知
    boolean handleNotify(Map<String, String> params);

    //主动向支付宝查询订单状态
    boolean queryAndCompensate(String orderNo);
}
