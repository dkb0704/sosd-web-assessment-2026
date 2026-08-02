package com.example.computingpowerrental.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Lark
 * @ date 2026/7/16  11:44
 * @ description AI模型实体类
 */
@Data
public class AiModel {
    private Long id;   //模型主键ID
    private String modelCode;   //模型唯一编码
    private String modelName;   //模型展示名称
    private String provider;   //模型服务提供商
    private String category;   //模型支持的内容分类
    private Integer costPoints;   //每次调用消耗的算力点数
    private Integer status;   //模型状态，0未上架，1已上架，2已下架
    private String description;   //模型描述（模型能力、适用场景等）
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
