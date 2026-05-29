package com.fzujxl.aicomputerplatform.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String userName;
    private String password;
    private String email;
    private BigDecimal point;
    private String role;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private LocalDateTime lastSignDate;
    private Integer signDays;
}
