package com.example.computingpowerrental.service;

import com.example.computingpowerrental.entity.RechargeOrder;
import com.example.computingpowerrental.vo.RechargeOrderPageVO;

/**
 * @author Lark
 * @ date 2026/8/16  15:14
 * @ description 充值订单业务接口
 */
public interface RechargeOrderService {
    //创建充值订单
    RechargeOrder createOrder(Long userId, Long packageId);

    //查询当前用户的订单详情
    RechargeOrder getUserOrder(Long userId, Long orderId);

    //分页查询当前用户充值订单
    RechargeOrderPageVO pageUserOrders(Long userId, Integer page, Integer size, Integer status);

    //根据平台订单号查询
    RechargeOrder getByOrderNo(String orderNo);

    //处理支付成功
    void completePayment(String orderNo, String tradeNo);

    //关闭超时未支付订单
    int closeTimeoutOrders();

    //用户主动取消订单
    void cancelOrder(Long userId, Long orderId);

    //管理端分页查询全站充值订单
    RechargeOrderPageVO adminPageOrders(Integer page, Integer size, Integer status);

    //管理端查询充值订单详情
    RechargeOrder adminGetOrder(Long orderId);

    //管理端异常订单手动补单
    void adminCompleteOrder(Long orderId);
}
