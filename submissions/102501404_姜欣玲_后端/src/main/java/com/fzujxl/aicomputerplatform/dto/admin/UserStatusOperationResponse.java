package com.fzujxl.aicomputerplatform.dto.admin;

import lombok.Data;

@Data
public class UserStatusOperationResponse {

    private Long userId;
    private String username;
    private Integer status;
}
