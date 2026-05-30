package com.aiplatform.controller.order;

import com.aiplatform.common.Result;
import com.aiplatform.dto.order.CreateOrderRequest;
import com.aiplatform.service.order.OrderService;
import com.aiplatform.mapper.ProductMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ProductMapper productMapper;

    @GetMapping("/products")
    public Result<?> listProducts() {
        return Result.success(productMapper.selectList(null));
    }

    @PostMapping("/order/create")
    public Result<?> createOrder(@Valid @RequestBody CreateOrderRequest req,
                                 HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(orderService.createOrder(userId, req.getProductId()));
    }

    @GetMapping("/order/list")
    public Result<?> getUserOrders(@RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(orderService.getUserOrders(userId, page, size));
    }

    /**
     * 支付宝支付回调（对外暴露，无需登陆）
     */
    @PostMapping("/order/callback")
    public Result<?> alipayCallback(@RequestParam Map<String, String> params) {
        orderService.handleCallback(params);
        return Result.success("success");
    }
}