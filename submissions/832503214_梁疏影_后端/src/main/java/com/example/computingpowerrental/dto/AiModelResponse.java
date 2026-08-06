package com.example.computingpowerrental.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Lark
 * @ date 2026/7/27  17:17
 * @ description 返回给前端的数据
 */
@Data
public class AiModelResponse {
    private Long id;
    private String modelCode;
    private String modelName;
    private String provider;
    private String category;
    private Integer costPoints;
    private Integer status;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
