package com.fzujxl.aicomputerplatform.dto.user;

import com.fzujxl.aicomputerplatform.validation.PermitNullButNotEmpty;
import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.NoArgsConstructor;

//修改个人信息时，前端传来的可选字段
@Data
@NoArgsConstructor
public class UpdateProfileRequest {
    @PermitNullButNotEmpty
    @Size(min = 3, max = 20, message = "用户名长度必须在3-20位之间")
    private String userName;

    @PermitNullButNotEmpty
    @Email(message = "邮箱格式不正确")
    private String email;
}