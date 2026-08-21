package com.fzujxl.aicomputerplatform.config;

import com.fzujxl.aicomputerplatform.interceptor.JwtInterceptor;
import com.fzujxl.aicomputerplatform.interceptor.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//拦截器注册
@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;
    private final RequestInterceptor requestInterceptor;

    public WebMvcConfig(JwtInterceptor jwtInterceptor, RequestInterceptor requestInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
        this.requestInterceptor = requestInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/api/auth/**",
                        "/api/user/logout",
                        "/api/user/me",
                        "/api/user/login",
                        "/api/gallery/**",
                        "/v3/api-docs/**",
                        "/swagger-ui/**"
                );
        registry.addInterceptor(requestInterceptor)
                .addPathPatterns("/api/gallery/artwork/");
    }
    //TODO 管理端拦截
}