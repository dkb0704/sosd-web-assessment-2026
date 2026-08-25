package com.example.computingpowerrental.enums;

import lombok.Getter;

/**
 * @author Lark
 * @ date 2026/7/16  16:39
 * @ description AI模型服务提供商枚举
 */
@Getter
public enum AiProvider {
    //DeepSeek
    DEEPSEEK("deepseek", "DeepSeek"),

    //阿里云通义千问
    ALIBABA("alibaba", "Alibaba"),

    //OpenAI
    OPENAI("openai", "OpenAI");

    //数据库存储值
    private final String code;

    //展示名称
    private final String description;

    AiProvider(String code, String description) {
        this.code = code;
        this.description = description;
    }

    //根据 provider 编码获取枚举
    public static AiProvider fromCode(String code) {

        if (code == null || code.trim().isEmpty()) {
            return null;
        }

        for (AiProvider provider : values()) {

            if (provider.code.equalsIgnoreCase(code.trim())) {
                return provider;
            }

        }

        throw new IllegalArgumentException("不支持的 AI Provider：" + code);
    }

    //判断 Provider 是否合法
    public static boolean isValid(String code) {

        if (code == null || code.trim().isEmpty()) {
            return false;
        }

        for (AiProvider provider : values()) {

            if (provider.code.equalsIgnoreCase(code.trim())) {
                return true;
            }

        }

        return false;
    }
}
