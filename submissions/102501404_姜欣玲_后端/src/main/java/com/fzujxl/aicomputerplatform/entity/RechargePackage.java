package com.fzujxl.aicomputerplatform.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RechargePackage {
    private Long id;
    private String packageName;
    private String packageType;
    private String description;
    private Long originalPrice;
    private Long currentPrice;
    private Long points;
    private Long dailyPoints;
    private Integer duration;
    private Integer status;
    private Integer sortOrder;
    private Integer deleted;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;

}
