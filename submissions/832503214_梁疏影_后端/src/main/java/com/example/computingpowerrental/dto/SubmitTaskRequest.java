package com.example.computingpowerrental.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author Lark
 * @ date 2026/7/16  10:56
 * @ description 提交AI生成任务请求DTO
 */
@Data
public class SubmitTaskRequest {
    //用户提交的提示词
    @NotBlank(message = "提示词不能为空")
    @Size(max = 2000, message = "提示词长度不能超过2000个字符")
    private String prompt;

    //用户选择的 AI 模型 ID
    @NotNull(message = "请选择AI模型")
    private Long modelId;

    //内容分类
    @NotBlank(message = "作品分类不能为空")
    @Size(max = 30, message = "作品分类长度不能超过30个字符")
    private String category;

    //是否公开到作品画廊。
    private Boolean isPublic = false;
}
