package com.fzujxl.aicomputerplatform.interceptor;

import com.fzujxl.aicomputerplatform.common.Result;
import com.fzujxl.aicomputerplatform.common.ResultCode;
import com.fzujxl.aicomputerplatform.mapper.user.UserMapper;
import com.fzujxl.aicomputerplatform.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

import static com.fzujxl.aicomputerplatform.utils.RedisKeys.TOKEN_VERSION_KEY;

//JWT认证拦截器
@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;
    private final UserMapper userMapper;

    public JwtInterceptor(JwtUtil jwtUtil, StringRedisTemplate stringRedisTemplate, ObjectMapper objectMapper, UserMapper userMapper) {
        this.jwtUtil = jwtUtil;
        this.stringRedisTemplate = stringRedisTemplate;
        this.objectMapper = objectMapper;
        this.userMapper = userMapper;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @Nullable Object handler) throws Exception {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write(objectMapper.writeValueAsString(Result.error(ResultCode.UNAUTHORIZED, "未登录或 token 已失效")));
            writer.flush();
            return false;
        }

        String token = authHeader.substring(7);

        try {
            if (jwtUtil.isTokenExpired(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter writer = response.getWriter();
                writer.write(objectMapper.writeValueAsString(Result.error(ResultCode.UNAUTHORIZED, "未登录或 token 已失效")));
                writer.flush();
                return false;
            }

            String blacklistKey = jwtUtil.getBlacklistTokenKey(token);
            boolean hasKey;
            try {
                hasKey = stringRedisTemplate.hasKey(blacklistKey);
            } catch (Exception e) {
                log.error("查询Redis黑名单失败，降级放行: {}", e.getMessage());
                hasKey = false;
            }

            if (hasKey) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter writer = response.getWriter();
                writer.write(objectMapper.writeValueAsString(Result.error(ResultCode.UNAUTHORIZED, "未登录或 token 已失效")));
                writer.flush();
                return false;
            }

            Long userId = jwtUtil.getUserIdFromToken(token);
            String username = jwtUtil.getUsernameFromToken(token);
            String version = jwtUtil.getVersionFromToken(token);

            try {
                String tokenVersion = stringRedisTemplate.opsForValue().get(TOKEN_VERSION_KEY);
                if (version.equals(tokenVersion)) {
                    return true;
                }
            }catch (Exception e){
                log.error("查询Redis token 版本失败，查询数据库", e);
                return version.equals(String.valueOf(userMapper.selectVersion(username)));
            }

            request.setAttribute("userId", userId);
            request.setAttribute("username", username);
            request.setAttribute("version", version);

            return true;
        } catch (ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            log.error("JWT验证失败: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write(objectMapper.writeValueAsString(Result.error(ResultCode.UNAUTHORIZED, "未登录或 token 已失效")));
            writer.flush();
            return false;
        }
    }

    //TODO 管理端拦截
}