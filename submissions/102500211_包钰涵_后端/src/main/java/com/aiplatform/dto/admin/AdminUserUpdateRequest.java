package com.aiplatform.dto.admin;

import lombok.Data;

@Data
public class AdminUserUpdateRequest {
    private String nickname;
    private String avatar;
    private Integer status;
}