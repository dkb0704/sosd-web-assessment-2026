package com.aiplatform.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * AI 模型表
 */
@Data
@TableName("ai_model")
public class AiModel {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 模型标识（如 "text"、"image"） */
    private String modelKey;

    /** 模型名称（如 "文本对话"、"AI绘图"） */
    private String modelName;

    /** 每次调用消耗的算力点数 */
    private Integer costPerTask;

    /** 状态：0-禁用 1-启用 */
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    private Boolean allowTextInput;

    private Boolean allowImageInput;


}