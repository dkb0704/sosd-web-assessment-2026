package com.example.computingpowerrental.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author Lark
 * @ date 2026/7/27  17:14
 * @ description 修改AI模型基础信息请求DTO
 */
@Data
public class UpdateModelRequest {
    //模型展示名称
    @NotBlank(message = "模型名称不能为空")
    @Size(max = 100,
            message = "模型名称长度不能超过100个字符")
    private String modelName;


    //模型提供商
    @NotBlank(message = "模型提供商不能为空")
    @Size(max = 50,
            message = "模型提供商长度不能超过50个字符")
    private String provider;


    //模型分类
    @NotBlank(message = "模型分类不能为空")
    @Size(max = 30,
            message = "模型分类长度不能超过30个字符")
    private String category;


    //模型描述
    @Size(max = 255,
            message = "描述长度不能超过255个字符")
    private String description;
}
