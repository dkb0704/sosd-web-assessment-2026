package com.example.computingpowerrental.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.example.computingpowerrental.config.AlipayProperties;
import com.example.computingpowerrental.entity.RechargeOrder;
import com.example.computingpowerrental.enums.RechargeOrderStatus;
import com.example.computingpowerrental.service.AlipayService;
import com.example.computingpowerrental.service.RechargeOrderService;
import org.springframework.stereotype.Service;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author Lark
 * @ date 2026/8/26  18:48
 * @ description 支付宝沙箱支付业务实现类
 */
@Service
public class AlipayServiceImpl implements AlipayService{
    private static final String PRODUCT_CODE = "FAST_INSTANT_TRADE_PAY";

    private final AlipayClient alipayClient;

    private final AlipayProperties alipayProperties;

    private final RechargeOrderService rechargeOrderService;

    public AlipayServiceImpl(AlipayClient alipayClient, AlipayProperties alipayProperties, RechargeOrderService rechargeOrderService) {
        this.alipayClient = alipayClient;

        this.alipayProperties = alipayProperties;

        this.rechargeOrderService = rechargeOrderService;
    }

    //生成支付宝电脑网站支付页面
    @Override
    public String createPagePay(Long userId, Long orderId) {
        RechargeOrder order = rechargeOrderService.getUserOrder(userId, orderId);

        //只能支付待支付订单
        if (!RechargeOrderStatus.PENDING.getCode().equals(order.getStatus())) {
            throw new RuntimeException("当前订单不可支付");
        }

        //检查订单是否已过期
        if (order.getExpiredAt() != null && LocalDateTime.now().isAfter(order.getExpiredAt())) {
            throw new RuntimeException("订单已过期，请重新创建充值订单");
        }

        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();

        //支付宝服务器异步通知地址
        request.setNotifyUrl(alipayProperties.getNotifyUrl());

        AlipayTradePagePayModel model = new AlipayTradePagePayModel();

        //我们自己的订单号
        model.setOutTradeNo(order.getOrderNo());

        //支付金额，以数据库订单快照为准
        model.setTotalAmount(order.getAmount().setScale(2).toPlainString());

        //支付宝支付页面显示的商品标题
        model.setSubject("算力充值-" + order.getPackageName());

        //电脑网站支付固定值
        model.setProductCode(PRODUCT_CODE);

        //支付宝侧设置30分钟过期
        model.setTimeoutExpress("30m");

        request.setBizModel(model);

        try {
            AlipayTradePagePayResponse response = alipayClient.pageExecute(request);

            if (!response.isSuccess()) {
                throw new RuntimeException("支付宝支付请求失败：" + response.getSubMsg());
            }

            /*
             * 返回的是支付宝自动提交的HTML表单。
             * 浏览器渲染该HTML后会跳转到沙箱收银台。
             */
            return response.getBody();

        } catch (AlipayApiException e) {
            throw new RuntimeException("调用支付宝支付接口失败：" + e.getMessage(), e);
        }
    }

    //处理支付宝异步通知
    @Override
    public boolean handleNotify(Map<String, String> params) {
        try {
            //支付宝签名验证
            boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayProperties.getAlipayPublicKey(), alipayProperties.getCharset(), alipayProperties.getSignType());

            if (!signVerified) {
                return false;
            }

            //校验通知中的APPID
            String appId = params.get("app_id");

            if (!alipayProperties.getAppId().equals(appId)) {
                return false;
            }

            //取得交易状态
            String tradeStatus = params.get("trade_status");

            //不是支付成功状态时，不增加算力，但通知本身已经合法，可以告诉支付宝我们已经收到。
            if (!"TRADE_SUCCESS".equals(tradeStatus) && !"TRADE_FINISHED".equals(tradeStatus)) {
                return true;
            }

            String orderNo = params.get("out_trade_no");

            String tradeNo = params.get("trade_no");

            String totalAmount = params.get("total_amount");

            //第四步，根据订单号查找我们的订单
            RechargeOrder order = rechargeOrderService.getByOrderNo(orderNo);

            //第五步，必须验证支付宝返回金额
            BigDecimal notifyAmount = new BigDecimal(totalAmount);

            if (order.getAmount().compareTo(notifyAmount) != 0) {
                return false;
            }

            //第六步，完成订单并增加用户算力
            rechargeOrderService.completePayment(orderNo, tradeNo);
            return true;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    /**
     * 主动查询支付宝订单状态并进行掉单补偿
     */
    @Override
    public boolean queryAndCompensate(String orderNo) {

        if (orderNo == null || orderNo.isBlank()) {
            return false;
        }

        try {
            RechargeOrder order = rechargeOrderService.getByOrderNo(orderNo);

            //本地已经支付，不需要再次处理
            if (RechargeOrderStatus.PAID.getCode().equals(order.getStatus())) {
                return true;
            }

            //待支付或已被本地超时关闭的订单均可向支付宝核实。
            if (!RechargeOrderStatus.PENDING.getCode().equals(order.getStatus())
                    && !RechargeOrderStatus.CLOSED.getCode().equals(order.getStatus())) {
                return false;
            }

            AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();

            /*
             * 使用平台订单号查询支付宝交易。
             * out_trade_no 就是创建支付时传给支付宝的 orderNo。
             */
            request.setBizContent("{\"out_trade_no\":\"" + orderNo + "\"}");

            AlipayTradeQueryResponse response = alipayClient.execute(request);

            if (!response.isSuccess()) {
                /*
                 * 例如订单尚未真正创建到支付宝侧，
                 * 这里不认为是系统异常，也不修改本地订单。
                 */
                return false;
            }

            String tradeStatus = response.getTradeStatus();

            //支付宝侧仍然等待用户付款
            if ("WAIT_BUYER_PAY".equals(tradeStatus)) {
                return false;
            }

            //只有支付宝明确确认支付完成，才进行补单
            if (!"TRADE_SUCCESS".equals(tradeStatus)
                    && !"TRADE_FINISHED".equals(tradeStatus)) {
                return false;
            }

            /*
             * 主动查单同样校验支付金额。
             * 防止异常情况下给错误订单增加算力。
             */
            String totalAmount = response.getTotalAmount();

            if (totalAmount == null) {
                return false;
            }

            BigDecimal alipayAmount = new BigDecimal(totalAmount);

            if (order.getAmount().compareTo(alipayAmount) != 0) {
                return false;
            }

            /*
             * 复用已有支付成功逻辑：
             * PENDING -> PAID
             * 保存支付宝交易号
             * 增加用户算力
             */
            rechargeOrderService.completePayment(orderNo, response.getTradeNo()
            );

            return true;

        } catch (AlipayApiException e) {

            System.err.println("支付宝主动查单失败，订单号：" + orderNo + "，原因：" + e.getMessage());

            return false;
        } catch (Exception e) {

            System.err.println("订单补偿处理失败，订单号：" + orderNo + "，原因：" + e.getMessage());

            return false;
        }
    }
}
