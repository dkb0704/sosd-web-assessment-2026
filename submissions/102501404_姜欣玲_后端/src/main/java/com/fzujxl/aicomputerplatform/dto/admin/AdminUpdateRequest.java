package com.fzujxl.aicomputerplatform.dto.admin;

import com.fzujxl.aicomputerplatform.validation.PermitNullButNotEmpty;
import com.fzujxl.aicomputerplatform.validation.ValidRole;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminUpdateRequest {
    @NotNull
    @NotEmpty
    @Min(1)
    private Long id;

    @PermitNullButNotEmpty
    @ValidRole(message = "角色格式错误,必须是admin或user")
    private String role;

    @PermitNullButNotEmpty
    @Size(min = 3,max = 20,message = "用户名长度必须在3到20之间")
    private String userName;

    @PermitNullButNotEmpty
    @Email(message = "邮箱格式不正确")
    private String email;
}
