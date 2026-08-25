package com.aiplatform.service.order.impl;

import com.aiplatform.client.alipay.AlipayPaymentClient;
import com.aiplatform.common.BusinessException;
import com.aiplatform.config.AlipayConfig;
import com.aiplatform.dto.order.CreateOrderResponse;
import com.aiplatform.dto.order.OrderListResponse;
import com.aiplatform.entity.Product;
import com.aiplatform.entity.RechargeOrder;
import com.aiplatform.mapper.ProductMapper;
import com.aiplatform.mapper.RechargeOrderMapper;
import com.aiplatform.mapper.UserMapper;
import com.aiplatform.service.order.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.alipay.api.internal.util.AlipaySignature;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ProductMapper productMapper;
    private final RechargeOrderMapper orderMapper;
    private final UserMapper userMapper;
    private final AlipayPaymentClient alipayClient;
    private final AlipayConfig alipayConfig;

    @Transactional
    @Override
    public CreateOrderResponse createOrder(Long userId, Long productId) {
        Product product = productMapper.selectById(productId);
        if (product == null || product.getStatus() != 1) {
            throw new BusinessException("套餐不存在或已下架");
        }

        // 生成订单号
        String orderNo = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + userId;
        RechargeOrder order = new RechargeOrder();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setProductId(productId);
        order.setAmount(product.getPrice());
        order.setPointsGained(product.getPoints());
        order.setStatus(0);
        order.setCreateTime(LocalDateTime.now());
        orderMapper.insert(order);

        // 调用支付宝生成支付表单
        String form = alipayClient.pagePay(orderNo, product.getName(), product.getPrice().toString());
        return CreateOrderResponse.builder()
                .orderNo(orderNo)
                .qrCode(form) // 完整HTML表单，前端可直接渲染或提取二维码
                .build();
    }

    @Transactional
    @Override
    public void handleCallback(Map<String, String> params) {
        try {
            boolean signVerified = AlipaySignature.rsaCheckV1(params,
                    alipayConfig.getAlipayPublicKey(), "UTF-8", "RSA2");
            if (!signVerified) {
                log.error("支付宝回调验签失败");
                throw new BusinessException("验签失败");
            }
        } catch (Exception e) {
            throw new BusinessException("验签异常");
        }

        String orderNo = params.get("out_trade_no");
        String tradeStatus = params.get("trade_status");
        if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
            RechargeOrder order = orderMapper.selectById(orderNo);
            if (order != null && order.getStatus() == 0) {
                order.setStatus(1);
                order.setPayTime(LocalDateTime.now());
                order.setAlipayTradeNo(params.get("trade_no"));
                orderMapper.updateById(order);
                // 增加用户算力
                userMapper.updatePoints(order.getUserId(), order.getPointsGained());
            }
        }
    }

    @Override
    public Page<OrderListResponse> getUserOrders(Long userId, int page, int size) {
        Page<RechargeOrder> orderPage = orderMapper.selectPage(
                new Page<>(page, size),
                new LambdaQueryWrapper<RechargeOrder>()
                        .eq(RechargeOrder::getUserId, userId)
                        .orderByDesc(RechargeOrder::getCreateTime));
        List<OrderListResponse> list = orderPage.getRecords().stream()
                .map(o -> OrderListResponse.builder()
                        .orderNo(o.getOrderNo())
                        .amount(o.getAmount())
                        .pointsGained(o.getPointsGained())
                        .status(o.getStatus())
                        .createTime(o.getCreateTime())
                        .payTime(o.getPayTime())
                        .build())
                .collect(Collectors.toList());
        Page<OrderListResponse> result = new Page<>(page, size, orderPage.getTotal());
        result.setRecords(list);
        return result;
    }

    @Transactional
    @Override
    public void updateOrderStatus(String orderNo, Integer status) {
        RechargeOrder order = orderMapper.selectById(orderNo);
        if (order == null) throw new BusinessException(404, "订单不存在");
        if (status == 1 && order.getStatus() != 1) {
            // 手动改为已支付时，需补加算力
            order.setStatus(1);
            order.setPayTime(LocalDateTime.now());
            orderMapper.updateById(order);
            userMapper.updatePoints(order.getUserId(), order.getPointsGained());
        } else {
            order.setStatus(status);
            orderMapper.updateById(order);
        }
    }

    @Override
    public void compensateOrder(String orderNo) {
        RechargeOrder order = orderMapper.selectById(orderNo);
        if (order != null && order.getStatus() == 0) {
            boolean paid = alipayClient.queryOrderPaySuccess(orderNo);
            if (paid) {
                updateOrderStatus(orderNo, 1);
            }
        }
    }
}