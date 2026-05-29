package com.fzujxl.aicomputerplatform.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginRequest {

        @NotBlank(message = "账号不能为空")
        private String account;

        @NotBlank(message = "密码不能为空")
        private String password;

}
