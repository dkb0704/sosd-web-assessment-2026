package com.example.computingpowerrental.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Lark
 * @ date 2026/7/16  10:31
 * @ description AI生成任务实体类
 */
@Data
public class AiTask {
    private Long id;   //AI任务ID，数据库主键，自增生成
    private Long userId;   //提交任务的用户ID
    private String prompt;   //用户提交的提示词
    private String modelName;   //任务所使用的AI模型
    private String category;   //内容分类
    private Integer costPoints;   //本次任务实际消耗的算力点数
    private Integer status;  //任务状态，0排队中，1生成中，2生成成功，3生成失败
    private String result;   //AI生成结果
    private String errorMessage;   //任务失败原因
    private Boolean isPublic;   //是否公开到作品画廊
    private Integer likeCount;   //点赞数量
    private LocalDateTime createdAt;
    private LocalDateTime finishedAt;
    private LocalDateTime updatedAt;
}
