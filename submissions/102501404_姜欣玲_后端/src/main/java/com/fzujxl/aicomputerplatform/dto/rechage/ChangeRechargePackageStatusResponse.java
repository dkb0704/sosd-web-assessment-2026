package com.fzujxl.aicomputerplatform.dto.rechage;

import java.time.LocalDateTime;

public record ChangeRechargePackageStatusResponse(
        Integer status,
        Long id,
        LocalDateTime updatedTime
){}
