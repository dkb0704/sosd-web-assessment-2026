package com.aiplatform.service.order;

import com.aiplatform.dto.order.CreateOrderResponse;
import com.aiplatform.dto.order.OrderListResponse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Map;

public interface OrderService {
    CreateOrderResponse createOrder(Long userId, Long productId);
    void handleCallback(Map<String, String> params);
    Page<OrderListResponse> getUserOrders(Long userId, int page, int size);
    void updateOrderStatus(String orderNo, Integer status); // 管理端手动处理
    void compensateOrder(String orderNo); // 掉单补偿
}