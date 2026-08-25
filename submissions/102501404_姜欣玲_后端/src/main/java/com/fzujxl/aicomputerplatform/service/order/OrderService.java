package com.fzujxl.aicomputerplatform.service.order;

import com.alipay.api.AlipayApiException;
import com.fzujxl.aicomputerplatform.dto.PageRequest;
import com.fzujxl.aicomputerplatform.dto.PageResultResponse;
import com.fzujxl.aicomputerplatform.dto.order.OrderCreateRequest;
import com.fzujxl.aicomputerplatform.dto.order.OrderCreateResponse;
import com.fzujxl.aicomputerplatform.entity.Order;
import jakarta.servlet.http.HttpServletRequest;

public interface OrderService {
    PageResultResponse<Order> pageQuery(PageRequest request);

    String createPayForm(String orderNo) throws AlipayApiException;

    void handlePayNotify(HttpServletRequest request) throws Exception;

    OrderCreateResponse createOrder(OrderCreateRequest request, Long userId);
}
