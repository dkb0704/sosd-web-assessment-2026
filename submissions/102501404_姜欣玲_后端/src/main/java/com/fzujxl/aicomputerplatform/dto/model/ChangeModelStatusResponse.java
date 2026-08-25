package com.fzujxl.aicomputerplatform.dto.model;

import java.time.LocalDateTime;

public record ChangeModelStatusResponse (
        Long id,
        Integer status,
        LocalDateTime updatedTime
 ){}
