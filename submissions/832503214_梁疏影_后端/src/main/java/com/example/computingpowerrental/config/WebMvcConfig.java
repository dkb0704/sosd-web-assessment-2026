package com.example.computingpowerrental.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Lark
 * @ date 2026/5/25  15:15
 * @ description Web MVC配置类
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer{
    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        // 认证相关接口
                        "/api/auth/login",
                        "/api/auth/register",
                        "/api/auth/refresh",

                        // 支付宝异步回调，必须放行
                        "/api/payment/alipay/notify",

                        // 开发测试接口
                        "/api/test/**",

                        // Swagger / Knife4j
                        "/doc.html",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",

                        // 系统基础路径
                        "/error",
                        "/favicon.ico"
                );
    }
}
