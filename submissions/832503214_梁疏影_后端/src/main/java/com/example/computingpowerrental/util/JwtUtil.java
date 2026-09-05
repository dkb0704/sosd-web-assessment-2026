package com.example.computingpowerrental.util;

import com.example.computingpowerrental.util.EnvUtil;
import com.example.computingpowerrental.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lark
 * @ date 2026/5/24  17:33
 * @ description JWT工具类
 */
@Component
public class JwtUtil {
    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private EnvUtil envUtil;

    //生成Access Token
    public String generateAccessToken(Long userId, String username, Integer role) {
        return generateToken(userId, username, role, "access", jwtProperties.getAccessTokenExpiration());
    }

    //生成Refresh Token
    public String generateRefreshToken(Long userId, String username, Integer role) {
        return generateToken(userId, username, role, "refresh", jwtProperties.getRefreshTokenExpiration());
    }

    //生成Token（基础方法）
    private String generateToken(Long userId,
                                 String username,
                                 Integer role,
                                 String tokenType,
                                 long expiration) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("role", role);
        claims.put("type", tokenType);

        if (envUtil != null) {
            claims.put("env", envUtil.getActiveProfile());
        }

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(
                        Keys.hmacShaKeyFor(getSecretKeyString().getBytes()),
                        SignatureAlgorithm.HS512
                )
                .compact();
    }

    //从Token中解析Claims
    public Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(
                            Keys.hmacShaKeyFor(
                                    getSecretKeyString().getBytes()
                            )
                    )
                    .parseClaimsJws(token)
                    .getBody();

        } catch (JwtException e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "JWT解析失败: " + e.getMessage(),
                    e
            );
        }
    }

    //从Token中获取用户ID
    public Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        Object userId = claims.get("userId");
        //处理可能的类型转换
        if (userId instanceof Integer) {
            return ((Integer) userId).longValue();
        } else if (userId instanceof Long) {
            return (Long) userId;
        } else if (userId instanceof Number) {
            return ((Number) userId).longValue();
        }else if (userId instanceof String) {
            return Long.parseLong((String) userId);
        }
        return null;
    }

    //从Token中获取用户名
    public String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        return (String) claims.get("username");
    }

    //获取用户名
    public Integer getRoleFromToken(String token) {
        Claims claims = parseToken(token);
        Object role = claims.get("role");

        if (role instanceof Integer) {
            return (Integer) role;
        } else if (role instanceof Number) {
            return ((Number) role).intValue();
        } else if (role instanceof String) {
            return Integer.parseInt((String) role);
        }

        return null;
    }

    //获取token类型
    public String getTokenType(String token) {
        Claims claims = parseToken(token);
        return (String) claims.get("type");
    }

    //判断是否为access token
    public boolean isAccessToken(String token) {
        return "access".equals(getTokenType(token));
    }

    //判断是否为refresh token
    public boolean isRefreshToken(String token) {
        return "refresh".equals(getTokenType(token));
    }

    //验证Token是否有效
    public boolean validateToken(String token) {
        try {

            Claims claims = parseToken(token);

            System.out.println(
                    "JWT解析成功:"
            );

            System.out.println(
                    "userId="
                            + claims.get("userId")
            );

            System.out.println(
                    "username="
                            + claims.get("username")
            );

            System.out.println(
                    "type="
                            + claims.get("type")
            );

            return true;

        } catch (Exception e) {

            System.out.println(
                    "JWT验证失败:"
            );

            e.printStackTrace();

            return false;
        }
    }

    //获取Token剩余时间（秒）
    public long getRemainingTime(String token) {
        try {
            Claims claims = parseToken(token);
            Date expiration = claims.getExpiration();
            long remainingMillis = expiration.getTime() - System.currentTimeMillis();
            return Math.max(0, remainingMillis / 1000);
        } catch (Exception e) {
            return 0;
        }
    }

    //获取Token的Claims
    public Claims getClaimsFromToken(String token) {
        return parseToken(token);
    }

    //获取密钥
    private String getSecretKeyString() {
        String secret = envUtil.getJwtSecret();

        if (secret == null || secret.trim().isEmpty()) {
            secret = jwtProperties.getSecretKey();
        }

        if (secret == null || secret.trim().isEmpty()) {
            throw new RuntimeException("JWT密钥未配置");
        }

        if (secret.length() < 64) {
            if (envUtil.isDevelopment()) {
                secret = String.format("%-64s", secret).replace(' ', 'X');
            } else {
                throw new RuntimeException("生产环境 JWT 密钥长度不能小于64位");
            }
        }

        return secret;
    }
}
