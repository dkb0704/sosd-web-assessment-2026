package com.example.computingpowerrental.enums;

import lombok.Getter;

/**
 * @author Lark
 * @ date 2026/7/16  16:37
 * @ description AI内容分类枚举
 */
@Getter
public enum AiCategory {
    //通用对话、问答和文本生成
    CHAT("chat", "对话"),

    //图片生成或图像相关任务
    IMAGE("image", "图片"),

    //代码生成、解释和优化
    CODE("code", "代码"),

    //文案、标题、宣传内容等生成
    COPYWRITING("copywriting", "文案");

    //保存到数据库中的分类编码
    private final String code;

    //用于前端展示或日志记录的中文说明
    private final String description;

    AiCategory(String code, String description) {
        this.code = code;
        this.description = description;
    }

    //根据分类编码获取对应枚举
    public static AiCategory fromCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return null;
        }

        for (AiCategory category : values()) {
            if (category.code.equalsIgnoreCase(code.trim())) {
                return category;
            }
        }

        throw new IllegalArgumentException("不支持的 AI 分类：" + code);
    }

    //判断某个分类编码是否合法
    public static boolean isValid(String code) {
        if (code == null || code.trim().isEmpty()) {
            return false;
        }

        for (AiCategory category : values()) {
            if (category.code.equalsIgnoreCase(code.trim())) {
                return true;
            }
        }

        return false;
    }
}
