package com.example.computingpowerrental.dto;

import lombok.Data;

/**
 * @author Lark
 * @ date 2026/8/6  16:40
 * @ description 用户端模型列表响应DTO，只返回用户选择模型时需要的信息
 */
@Data
public class AiModelSimpleResponse {
    //模型ID
    private Long id;
    //模型展示名称
    private String modelName;
    //模型提供商
    private String provider;
    //支持分类
    private String category;
    //每次调用消耗算力
    private Integer costPoints;
    //模型描述
    private String description;
    public AiModelSimpleResponse() {
    }
    public AiModelSimpleResponse(
            Long id,
            String modelName,
            String provider,
            String category,
            Integer costPoints,
            String description
    ) {
        this.id = id;
        this.modelName = modelName;
        this.provider = provider;
        this.category = category;
        this.costPoints = costPoints;
        this.description = description;
    }
}
