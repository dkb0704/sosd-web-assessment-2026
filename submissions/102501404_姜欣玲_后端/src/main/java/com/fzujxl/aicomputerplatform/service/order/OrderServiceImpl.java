package com.fzujxl.aicomputerplatform.service.order;

import com.alipay.api.internal.util.AlipaySignature;
import com.fzujxl.aicomputerplatform.common.ResultCode;
import com.fzujxl.aicomputerplatform.config.AliPayConfig;
import com.fzujxl.aicomputerplatform.dto.PageRequest;
import com.fzujxl.aicomputerplatform.dto.PageResultResponse;
import com.fzujxl.aicomputerplatform.dto.order.OrderCreateRequest;
import com.fzujxl.aicomputerplatform.dto.order.OrderCreateResponse;
import com.fzujxl.aicomputerplatform.entity.Order;
import com.fzujxl.aicomputerplatform.entity.RechargePackage;
import com.fzujxl.aicomputerplatform.exception.BusinessException;
import com.fzujxl.aicomputerplatform.mapper.order.OrderMapper;
import com.fzujxl.aicomputerplatform.utils.SnowFlake;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.json.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private static final String GATEWAY_URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private static final String FORMAT = "JSON";
    private static final String CHARSET = "UTF-8";
    private static final String SIGN_TYPE = "RSA2";

    private final AliPayConfig aliPayConfig;
    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderMapper orderMapper,AliPayConfig aliPayConfig) {
        this.orderMapper = orderMapper;
        this.aliPayConfig = aliPayConfig;
    }

    @Override
    public PageResultResponse<Order> pageQuery(PageRequest request) {
        log.info("充值订单分页查询: {}", request);
        PageHelper.startPage(request.getPageNum(), request.getPageSize());
        List<Order> orders = orderMapper.selectAllOrdersInfo();
        PageInfo<Order> pageInfo = new PageInfo<>(orders);
        return PageResultResponse.of(pageInfo);
    }

    @Override
    public OrderCreateResponse createOrder(OrderCreateRequest request, Long userId) {
        log.info("充值订单创建: {}", request);
        RechargePackage rechargePackage = orderMapper.selectByPackageId(request.getPackageId());
        if (rechargePackage == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "充值套餐不存在");
        }

        Order order = new Order();
        String orderNo = SnowFlake.nextIdStr();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setPackageId(request.getPackageId());
        order.setPackageName(rechargePackage.getPackageName());
        order.setPackageType(rechargePackage.getPackageType());
        order.setPoints(rechargePackage.getPoints());
        order.setDailyPoints(rechargePackage.getDailyPoints());
        order.setDuration(rechargePackage.getDuration());
        order.setAmount(rechargePackage.getCurrentPrice());

        int result = orderMapper.insertOrder(order,userId);
        if (result <= 0) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "订单创建失败");
        }

        return new OrderCreateResponse(orderNo);
    }

    /**
     * 创建支付表单
     */
    @Override
    public String createPayForm(String orderNo) throws AlipayApiException {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "订单不存在");
        }

        AlipayClient alipayClient = new DefaultAlipayClient(
                GATEWAY_URL,
                aliPayConfig.getAppId(),
                aliPayConfig.getAppPrivateKey(),
                FORMAT,
                CHARSET,
                aliPayConfig.getAlipayPublicKey(),
                SIGN_TYPE
        );

        AlipayTradePagePayRequest request = getAlipayTradePagePayRequest(order);

        return alipayClient.pageExecute(request).getBody();
    }

    /**
     * 处理支付异步通知
     */
    public void handlePayNotify(HttpServletRequest request) throws Exception {
        // 验签逻辑...
        if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
            System.out.println("=========支付宝异步回调========");

            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (String name : requestParams.keySet()) {
                params.put(name, request.getParameter(name));
            }

            String sign = params.get("sign");
            String content = AlipaySignature.getSignCheckContentV1(params);
            boolean checkSignature = AlipaySignature.rsa256CheckContent(content, sign, aliPayConfig.getAlipayPublicKey(), "UTF-8"); // 验证签名
            // 支付宝验签
            if (checkSignature) {
                // 验签通过
                System.out.println("交易名称: " + params.get("subject"));
                System.out.println("交易状态: " + params.get("trade_status"));
                System.out.println("支付宝交易凭证号: " + params.get("trade_no"));
                System.out.println("商户订单号: " + params.get("out_trade_no"));
                System.out.println("交易金额: " + params.get("total_amount"));
                System.out.println("买家在支付宝唯一id: " + params.get("buyer_id"));
                System.out.println("买家付款时间: " + params.get("gmt_payment"));
                System.out.println("买家付款金额: " + params.get("buyer_pay_amount"));

                String orderNo = params.get("out_trade_no");//订单号
                String gmtPayment = params.get("gmt_payment");//买家付款时间
                String alipayTradeNo = params.get("trade_no");//支付宝交易凭证号
                String packageName = params.get("subject");//交易名称
                Order order = new Order();
                order.setOrderNo(orderNo);
                order.setStatus("已支付");
                order.setPaymentTime(LocalDateTime.parse(gmtPayment));
                order.setTradeNo(alipayTradeNo);
                order.setPackageName(packageName);
                int result = orderMapper.updateByOrderNo(order);
                if(result<=0){
                    throw new BusinessException(ResultCode.INTERNAL_ERROR,"更新订单状态失败");
                }
            }
        }
    }

    private @NonNull AlipayTradePagePayRequest getAlipayTradePagePayRequest(Order order) {
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());
        request.setReturnUrl("http://localhost:8080/order");

        JSONObject bizContent = new JSONObject();
        bizContent.set("out_trade_no", order.getOrderNo());//订单号
        bizContent.set("total_amount", order.getAmount());//交易金额
        bizContent.set("subject", order.getPackageName());//交易名称
        bizContent.set("product_code", "FAST_INSTANT_TRADE_PAY");
        request.setBizContent(bizContent.toString());
        return request;
    }
}
