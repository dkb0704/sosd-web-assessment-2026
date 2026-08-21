package com.fzujxl.aicomputerplatform.dto.sign;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SignResponse {
    private Integer earnedPoints;     // 本次获得点数
    private Integer streakDays;       // 连续签到天数
    private BigDecimal totalPoints;   // 签到后总点数

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastSignDate;
}
