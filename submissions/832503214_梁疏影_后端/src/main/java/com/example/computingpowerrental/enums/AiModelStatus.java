package com.example.computingpowerrental.enums;

import lombok.Getter;

/**
 * @author Lark
 * @ date 2026/7/16  11:48
 * @ description AI模型状态枚举
 */
@Getter
public enum AiModelStatus {
    //模型已添加，但管理员尚未上架
    NOT_PUBLISHED(0, "未上架"),

    //模型已上架，可供用户使用
    PUBLISHED(1, "已上架"),

    //模型已下架，不允许继续调用
    UNPUBLISHED(2, "已下架");

    //数据库存储的状态码
    private final Integer code;

    //状态说明
    private final String description;

    AiModelStatus(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    //根据状态码获取枚举
    public static AiModelStatus fromCode(Integer code) {

        if (code == null) {
            return null;
        }

        for (AiModelStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }

        throw new IllegalArgumentException("未知模型状态：" + code);
    }
}
