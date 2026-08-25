package com.fzujxl.aicomputerplatform.interceptor;

import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RequestInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(@Nullable HttpServletRequest request,
                             @Nullable HttpServletResponse response,
                             @Nullable Object handler)  {
        assert request != null;
        String ip = JakartaServletUtil.getClientIP( request);
        System.out.println("用户真实IP：" + ip);
        return true;
    }
}
