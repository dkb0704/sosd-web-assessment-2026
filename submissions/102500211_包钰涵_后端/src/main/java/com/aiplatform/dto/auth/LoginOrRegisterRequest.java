package com.aiplatform.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginOrRegisterRequest {
    @NotBlank(message = "登录类型不能为空")
    private String type;  // password / code

    @Email(message = "邮箱格式错误")
    @NotBlank(message = "邮箱不能为空")
    private String email;

    private String password;
    private String code;
}