package com.fzujxl.aicomputerplatform.dto.admin;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PointOperateResponse {
    private Long id;
    private String userName;
    private BigDecimal point;
}
