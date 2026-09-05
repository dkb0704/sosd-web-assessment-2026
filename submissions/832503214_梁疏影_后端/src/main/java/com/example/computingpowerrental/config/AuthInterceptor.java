package com.example.computingpowerrental.config;

import com.example.computingpowerrental.util.JwtUtil;
import com.example.computingpowerrental.util.RequestHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import com.example.computingpowerrental.util.RedisUtil;

import java.io.IOException;

/**
 * @author Lark
 * @ date 2026/5/25  14:24
 * @ description 认证拦截器
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final Integer ROLE_ADMIN = 1;
    private static final String TOKEN_BLACKLIST_PREFIX = "auth:blacklist:";   //Redis 中 JWT 黑名单的 Key 前缀

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //如果不是映射到方法，直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String token = extractToken(request);

        System.out.println("收到Token:");
        System.out.println(token);

        if (token == null || token.trim().isEmpty()) {
            sendErrorResponse(response, 401, "未提供 Token 或 Token 格式错误");
            return false;
        }

        //验证Token并提取UserID
        try {
            //先简单校验有效性
            boolean valid = jwtUtil.validateToken(token);

            System.out.println(
                    "validateToken结果=" + valid
            );

            if (!valid) {
                sendErrorResponse(response, 401, "Token 无效或已过期");
                return false;
            }
            //检查token是否已经被加入Redis黑名单
            String blacklistKey = TOKEN_BLACKLIST_PREFIX + token;
            if (redisUtil.hasKey(blacklistKey)) {
                sendErrorResponse(response, 401, "登录状态已失效，请重新登录");
                return false;
            }
            if (!jwtUtil.isAccessToken(token)) {
                sendErrorResponse(response, 401, "请使用 Access Token 访问接口");
                return false;
            }

            Long userId = jwtUtil.getUserIdFromToken(token);
            Integer role = jwtUtil.getRoleFromToken(token);

            System.out.println("Interceptor userId=" + userId);
            System.out.println("Interceptor role=" + role);

            System.out.println(
                    "isAccessToken="
                            + jwtUtil.isAccessToken(token)
            );

            if (userId == null) {
                sendErrorResponse(response, 401, "Token 中不包含用户 ID");
                return false;
            }

            RequestHolder.setUserIdAndRole(userId, role);

            if (isAdminPath(request) && !ROLE_ADMIN.equals(role)) {
                sendErrorResponse(response, 403, "无管理员权限");
                return false;
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(response, 401, "Token 解析失败");
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //请求结束后，清理ThreadLocal，防止内存泄漏
        RequestHolder.remove();
    }

    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || authHeader.trim().isEmpty()) {
            return null;
        }

        if (authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return authHeader;
    }

    private boolean isAdminPath(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/api/admin/");
    }

    private void sendErrorResponse(HttpServletResponse response,
                                   int code,
                                   String message) throws IOException {
        response.setStatus(code);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(
                "{\"code\":" + code + ",\"message\":\"" + message + "\",\"data\":null}"
        );
    }
}
