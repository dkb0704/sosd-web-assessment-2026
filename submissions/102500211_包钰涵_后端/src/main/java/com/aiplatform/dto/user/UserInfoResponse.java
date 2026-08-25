package com.aiplatform.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoResponse {
    private String id;
    private String email;
    private String nickname;
    private String avatar;
    private Integer points;
    private Integer status;
    private String role;  // 仅管理员可见
    private Boolean signedToday;
}