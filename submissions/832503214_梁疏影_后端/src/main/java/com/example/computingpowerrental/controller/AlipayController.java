package com.example.computingpowerrental.controller;

import com.example.computingpowerrental.dto.ApiResponse;
import com.example.computingpowerrental.service.AlipayService;
import com.example.computingpowerrental.util.RequestHolder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Lark
 * @ date 2026/8/26  18:53
 * @ description 支付宝支付接口
 */
@RestController
@RequestMapping("/api/payment/alipay")
public class AlipayController {
    private final AlipayService alipayService;

    public AlipayController(AlipayService alipayService) {
        this.alipayService = alipayService;
    }

    //发起支付宝支付
    @PostMapping("/pay/{orderId}")
    public ApiResponse<String> pay(@PathVariable Long orderId) {
        Long userId = RequestHolder.getUserId();

        String form = alipayService.createPagePay(userId, orderId);

        return ApiResponse.success("获取支付宝支付页面成功", form);
    }

    /*
     * 支付宝异步通知
     * 这个接口不要求JWT，WebMvcConfig中已经提前放行。
     */
    @PostMapping(value = "/notify", produces = MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8")
    public String notify(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();

        request.getParameterMap().forEach((key, values) -> {
            if (values != null && values.length > 0) {
                params.put(key, String.join(",", values));
            }
        }
        );

        boolean success = alipayService.handleNotify(params);

        /*
         * 支付宝要求成功处理后返回纯文本 success
         */
        return success ? "success" : "failure";
    }
}
