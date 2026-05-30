package com.aiplatform.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("comment")
public class Comment {
    @TableId(type = IdType.AUTO)
    private Long commentId;

    /** 作品ID（对应 creation_task 的 task_id） */
    private Long workId;

    /** 评论者用户ID */
    private Long userId;

    /** 评论内容，最大200字 */
    private String content;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}