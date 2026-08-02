package com.example.computingpowerrental.enums;

import lombok.Getter;

/**
 * @author Lark
 * @ date 2026/7/16  10:40
 * @ description AI任务状态枚举
 */
@Getter
public enum AiTaskStatus {
    //任务已创建，正在消息队列中等待处理
    WAITING(0, "排队中"),

    //消费者已获取任务，正在调用 AI 模型
    PROCESSING(1, "生成中"),

    //AI 内容生成成功
    SUCCESS(2, "生成成功"),

    //AI 内容生成失败
    FAILED(3, "生成失败");

    //保存到数据库中的状态码
    private final Integer code;

    //状态说明
    private final String description;

    AiTaskStatus(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    //根据数据库状态码获取对应枚举
    public static AiTaskStatus fromCode(Integer code) {
        if (code == null) {
            return null;
        }

        for (AiTaskStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }

        throw new IllegalArgumentException("未知的 AI 任务状态：" + code);
    }
}
