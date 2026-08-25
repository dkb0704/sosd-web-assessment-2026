package com.fzujxl.aicomputerplatform.dto.rechage;

import java.time.LocalDateTime;

public record AddRechargePackageResponse(
        Long id,
        String packageName,
        String packageType,
        String description,
        Long originalPrice,
        Long currentPrice,
        Long points,
        Long dailyPoints,
        Integer duration,
        Integer status,
        Integer sortOrder,
        LocalDateTime createTime,
        LocalDateTime updateTime,
        Integer deleted
){}
