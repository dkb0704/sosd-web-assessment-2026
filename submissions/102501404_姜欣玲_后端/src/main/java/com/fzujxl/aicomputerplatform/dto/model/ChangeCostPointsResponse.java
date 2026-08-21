package com.fzujxl.aicomputerplatform.dto.model;

import java.time.LocalDateTime;

public record ChangeCostPointsResponse (
        Long costPoints,
        Long id,
        LocalDateTime updatedTime
){}
