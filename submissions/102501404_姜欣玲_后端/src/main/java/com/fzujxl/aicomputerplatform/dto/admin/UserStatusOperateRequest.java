package com.fzujxl.aicomputerplatform.dto.admin;

import com.fzujxl.aicomputerplatform.validation.PermitNullButNotEmpty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserStatusOperateRequest {

    @PermitNullButNotEmpty
    @Min(1)
    private Long userId;

    @PermitNullButNotEmpty
    @Size(min = 3,max = 20,message = "用户名长度必须在3到20之间")
    private String userName;

    @PermitNullButNotEmpty
    @Email(message = "邮箱格式错误")
    private String userEmail;

    @NotBlank
    @Size(max = 1)
    private Integer status;

}
