package com.fzujxl.aicomputerplatform.utils;

import com.fzujxl.aicomputerplatform.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
public class JwtUtil {

    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    public JwtUtil(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        // 在构造函数中生成并缓存SecretKey
        this.secretKey = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Long userId, String account, Long tokenVersion) {
        Instant now = Instant.now();
        Instant expiry = now.plusMillis(jwtConfig.getExpiration());

        return Jwts.builder()
                .subject(account)
                .claim("userId", userId)
                .claim("tokenVersion", tokenVersion)
                .id(UUID.randomUUID().toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    private Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .clockSkewSeconds(jwtConfig.getClockSkewSeconds())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            // 日志中记录Token的SHA-256哈希前8位作为标识，避免泄露Token原文
            log.error("JWT解析失败，token标识: {}", generateFingerprint(token).substring(0, 8), e);
            // 重新抛出原始异常，以便上层调用者可以根据具体异常类型进行处理
            throw e;
        }
    }

    /**
     * 公开的获取 Claims 方法，供外部调用，实现一次解析，多次使用
     * @param token JWT
     * @return Claims
     */
    public Claims getAllClaimsFromToken(String token) {
        return parseToken(token);
    }

    public boolean isTokenExpired(String token) {
        try {
            parseToken(token);
            return false;
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        Object userIdObj = claims.get("userId");
        if (userIdObj instanceof Long) {
            return (Long) userIdObj;
        } else if (userIdObj instanceof Integer) {
            return ((Integer) userIdObj).longValue();
        } else {
            throw new JwtException("Token 中 userId 类型异常");
        }
    }

    public String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    public String getVersionFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    public String getJtiFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getId();
    }

    public String getBlacklistTokenKey(String token) {
        String jti = getJtiFromToken(token);
        if (jti != null) {
            return "jwt:blacklist:" + jti;
        } else {
            // 对于没有JTI的旧Token，使用SHA-256指纹作为回退方案
            String fingerprint = generateFingerprint(token);
            return "jwt:blacklist:" + fingerprint;
        }
    }

    /**
     * 为 token 生成 SHA-256 指纹，用于旧 Token 的黑名单标识
     * @param input JWT
     * @return SHA-256指纹
     */
    public String generateFingerprint(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // SHA-256是Java标准库保证支持的算法，理论上不会发生此异常
            throw new RuntimeException("SHA-256算法不可用", e);
        }
    }
}