package com.aiplatform.config;

import com.aiplatform.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC 配置：CORS + JWT 拦截器
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    /** 跨域配置 */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    /** 拦截器注册 */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                // 需要登录的路径
                .addPathPatterns(
                        "/api/user/**",
                        "/api/creation/**",
                        "/api/order/**",
                        "/api/admin/**",
                        "/api/gallery/works/*/like",        // 点赞需要登录
                        "/api/gallery/works/*/comments"     // 评论 POST/DELETE 需要登录
                )
                // 放行路径（无需登录）
                .excludePathPatterns(
                        "/api/auth/**",
                        "/api/models",
                        "/api/gallery/works",        // 只放行 GET /api/gallery/works（列表）
                        "/api/gallery/works/*/comments", // 放行 GET 评论列表
                        "/api/products",
                        "/api/order/callback"
                );
    }
}