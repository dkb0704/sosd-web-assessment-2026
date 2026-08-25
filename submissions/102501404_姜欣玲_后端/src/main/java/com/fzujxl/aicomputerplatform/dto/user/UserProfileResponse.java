package com.fzujxl.aicomputerplatform.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

//获取用户信息或修改资料成功后，返回给前端的用户数据
@Data
public class UserProfileResponse {
    private Long id;
    private String userName;
    private String email;
    private BigDecimal point;
    private String role;
    private Integer status;
    private boolean updated;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastSignDate;

    private Integer signDays;
}
