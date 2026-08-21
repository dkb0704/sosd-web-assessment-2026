package com.fzujxl.aicomputerplatform.dto.model;

import java.time.LocalDateTime;

public record  AddModelResponse (
        Long id,
        String modelKey,
        String modelType,
        String description,
        Long costPoints,
        Integer status,
        Integer sortOrder,
        LocalDateTime createTime,
        LocalDateTime updateTime,
        Integer deleted
){}
