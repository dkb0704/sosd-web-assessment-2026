package com.example.computingpowerrental.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

/**
 * @author Lark
 * @ date 2026/5/24  16:53
 * @ description 登录请求DTO
 */
@Data
public class LoginRequest {
    @NotBlank(message = "用户名或邮箱不能为空")
    private String account;

    @NotBlank(message = "密码不能为空")
    private String password;
}
