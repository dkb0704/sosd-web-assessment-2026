package com.example.computingpowerrental.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author Lark
 * @ date 2026/5/24  18:13
 * @ description 环境变量工具类
 */
@Component
public class EnvUtil {
    @Autowired
    private Environment environment;

    //获取JWT密钥。优先读取系统环境变量 JWT_SECRET，如果没有，再读取 application.properties 中的 jwt.secret-key
    public String getJwtSecret() {
        //优先从系统环境变量获取
        String envSecret = System.getenv("JWT_SECRET");
        if (envSecret != null && !envSecret.trim().isEmpty()) {
            return envSecret;
        }

        //从Spring配置获取
        return environment.getProperty("jwt.secret-key");
    }

    //获取当前激活的profile
    public String getActiveProfile() {
        String[] profiles = environment.getActiveProfiles();
        if (profiles.length > 0) {
            return profiles[0];
        }
        return "default";
    }

    //判断是否为生产环境
    public boolean isProduction() {
        String profile = getActiveProfile();
        return "prod".equals(profile) || "production".equals(profile);
    }

    //判断是否为开发环境
    public boolean isDevelopment() {
        String profile = getActiveProfile();
        return "dev".equals(profile)
                || "development".equals(profile)
                || "default".equals(profile);
    }
}
