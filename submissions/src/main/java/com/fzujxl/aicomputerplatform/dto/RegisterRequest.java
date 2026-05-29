package com.fzujxl.aicomputerplatform.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "用户名不能为空")
    @Size(min=3,max=20,message = "用户名长度必须在3到20之间")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min=8,max=32,message = "密码长度必须在8到32之间")
    private String password;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式错误")
    private String email;
}
