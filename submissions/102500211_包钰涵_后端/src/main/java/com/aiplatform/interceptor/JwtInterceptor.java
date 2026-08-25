package com.aiplatform.interceptor;

import com.aiplatform.common.BusinessException;
import com.aiplatform.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT 认证拦截器 —— 校验 Authorization 头中的 Bearer Token
 *
 * 注意：使用 @Component 注册并配合 WebMvcConfig 注册路径
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        // 放行 OPTIONS 预检请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 获取 Authorization 头
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new BusinessException(401, "未登录或 token 格式错误");
        }

        String token = authHeader.substring(7);

        // 校验 Token
        if (!jwtUtil.validateToken(token)) {
            throw new BusinessException(401, "token 无效或已过期");
        }

        // 解析 Claims 存入 request 供后续 Controller 获取
        Claims claims = jwtUtil.parseToken(token);
        request.setAttribute("claims", claims);
        request.setAttribute("userId", claims.get("userId", Long.class));
        request.setAttribute("role", claims.get("role", String.class));
        System.out.println("拦截器设置的 userId: " + claims.get("userId", Long.class));

        return true;
    }
}