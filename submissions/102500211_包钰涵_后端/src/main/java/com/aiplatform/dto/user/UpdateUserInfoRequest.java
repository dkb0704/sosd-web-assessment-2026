package com.aiplatform.dto.user;

import lombok.Data;

@Data
public class UpdateUserInfoRequest {
    private String nickname;
    private String avatar;
}