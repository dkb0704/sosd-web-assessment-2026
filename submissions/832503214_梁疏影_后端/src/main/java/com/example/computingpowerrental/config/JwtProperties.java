package com.example.computingpowerrental.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Lark
 * @ date 2026/5/24  17:57
 * @ description JWT配置类
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    //JWT密钥
    private String secretKey;

    //Access Token过期时间（秒），默认2小时
    private long accessTokenExpiration = 7200;
    //Refresh Token过期时间（秒），默认7天
    private long refreshTokenExpiration = 604800;

    //Token前缀
    private String tokenPrefix = "Bearer ";

    //Token在Header中的Key
    private String tokenHeader = "Authorization";

    //验证配置是否有效
    public boolean isValid() {
        return secretKey != null
                && !secretKey.trim().isEmpty()
                && secretKey.length() >= 32;
    }

    //获取安全警告信息（如果配置不安全）
    public String getSecurityWarning() {
        if (secretKey == null || secretKey.trim().isEmpty()) {
            return "警告：JWT 密钥未配置";
        }
        if (secretKey.contains("please-change")
                || secretKey.contains("default")
                || secretKey.contains("must-be-32-chars-long")) {
            return "警告：正在使用默认 JWT 密钥，生产环境不安全";
        }
        if (secretKey.length() < 32) {
            return "警告：密钥长度不足32位，建议使用更长的密钥！";
        }
        return null;
    }
}
