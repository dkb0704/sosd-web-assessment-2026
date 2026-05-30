package com.aiplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 点赞记录表
 */
@Data
@TableName("like_record")
public class LikeRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 作品 ID（即 creation_task 的 task_id） */
    private Long workId;

    /** 点赞用户 ID */
    private Long userId;

    private LocalDateTime createTime;
}