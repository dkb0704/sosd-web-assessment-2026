package com.fzujxl.aicomputerplatform.controller.order;

import com.fzujxl.aicomputerplatform.common.Result;
import com.fzujxl.aicomputerplatform.dto.PageRequest;
import com.fzujxl.aicomputerplatform.dto.PageResultResponse;
import com.fzujxl.aicomputerplatform.dto.order.OrderCreateRequest;
import com.fzujxl.aicomputerplatform.dto.order.OrderCreateResponse;
import com.fzujxl.aicomputerplatform.entity.Order;
import com.fzujxl.aicomputerplatform.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@Slf4j
@Tag(name = "算力充值与订单模块")
public class OrderController {

    private final OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    
    @GetMapping("/page")
    @Operation(summary = "充值订单分页查询")
    public Result<PageResultResponse<Order>> pageQuery(@Valid @RequestBody PageRequest request) {
        log.info("充值订单分页查询: {}", request);
        PageResultResponse<Order> response = orderService.pageQuery(request);
        return Result.success("查询成功", response);
    }

    @PostMapping("/create")
    @Operation(summary = "充值订单创建")
    public Result<OrderCreateResponse> createOrder(@RequestAttribute("userId") Long userId,
                                                   @RequestBody OrderCreateRequest request) {
        log.info("充值订单创建: {}", request);
        OrderCreateResponse response = orderService.createOrder(request, userId);
        return Result.success("创建成功", response);
    }

    @GetMapping("/pay")//  /alipay/pay?orderNo=xxx
    @Operation(summary = "充值订单支付")
    public Result<String> pay(String orderNo) throws Exception {
        // 查询订单信息
        String form = orderService.createPayForm(orderNo);
        return Result.success(form);

    }

    @PostMapping("/notify")
    @Operation(summary = "充值订单支付通知")
    public Result<?> payNotify(HttpServletRequest request) throws Exception {
        orderService.handlePayNotify(request);
        return Result.success("处理成功");
    }
}
