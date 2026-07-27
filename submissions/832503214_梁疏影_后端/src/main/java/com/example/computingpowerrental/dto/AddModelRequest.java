package com.example.computingpowerrental.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author Lark
 * @ date 2026/7/16  16:35
 * @ description 新增AI模型请求DTO
 */
@Data
public class AddModelRequest {
    //模型唯一编码
    @NotBlank(message = "模型编码不能为空")
    @Size(max = 50, message = "模型编码长度不能超过50个字符")
    private String modelCode;

    //模型展示名称
    @NotBlank(message = "模型名称不能为空")
    @Size(max = 100, message = "模型名称长度不能超过100个字符")
    private String modelName;

    //模型提供商
    @NotBlank(message = "模型提供商不能为空")
    @Size(max = 50, message = "模型提供商长度不能超过50个字符")
    private String provider;

    //模型分类
    @NotBlank(message = "模型分类不能为空")
    @Size(max = 30, message = "模型分类长度不能超过30个字符")
    private String category;

    //每次调用消耗算力
    @NotNull(message = "算力消耗不能为空")
    @Min(value = 0, message = "算力消耗不能小于0")
    private Integer costPoints;

    //模型描述
    @Size(max = 255, message = "模型描述不能超过255个字符")
    private String description;
}
