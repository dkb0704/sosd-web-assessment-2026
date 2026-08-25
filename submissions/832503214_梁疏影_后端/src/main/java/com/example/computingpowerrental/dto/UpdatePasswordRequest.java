package com.example.computingpowerrental.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author Lark
 * @ date 2026/5/27  17:05
 * @ description 修改密码请求DTO
 */
@Data
public class UpdatePasswordRequest {
    //需要提供原密码以校验身份
    @NotBlank(message = "原密码不能为空")
    private String oldPassword;

    //新密码（要求6-30位；此处接收明文密码，在Service层加密后存储到数据库）
    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 30, message = "新密码长度应为6到30位")
    private String newPassword;
}
