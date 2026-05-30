package com.aiplatform.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * AI 生成任务表（同时也是画廊作品源，is_public=1 即为公开作品）
 */
@Data
@TableName("creation_task")
public class CreationTask {

    /** 任务 ID（雪花算法） */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)   // 加这行
    private Long taskId;

    /** 提交用户 ID */
    private Long userId;

    /** 提示词 */
    private String prompt;

    /** 使用的模型 ID */
    private Long modelId;

    /** 是否公开到画廊：0-不公开 1-公开 */
    private Integer isPublic=0;

    /** 任务状态：0-排队中 1-成功 2-失败 */
    private Integer status;

    /** 生成结果（文本内容或图片 URL） */
    private String result;

    /** 失败时的错误信息 */
    private String errorMsg;

    /** 本次消耗的算力点数 */
    private Integer pointsCost;

    /** 附件 URL（多个用逗号分隔） */
    private String fileUrls;

    /** 提交时间 */
    private LocalDateTime submitTime;

    /** 完成时间 */
    private LocalDateTime finishTime;

    /* ========== 以下为非数据库字段，用于关联查询 ========== */

    /** 模型分类（关联查询注入，如 "text"、"image"） */
    @TableField(exist = false)
    private String modelType;

    /** 作者昵称（关联查询注入） */
    @TableField(exist = false)
    private String nickname;

    /** 点赞数（关联查询注入） */
    @TableField(exist = false)
    private Integer likeCount;

    /** 当前用户是否已点赞（登录时返回） */
    @TableField(exist = false)
    private Boolean isLiked;
}