package com.fzujxl.aicomputerplatform.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Model {
    private Long id;
    private String modelName;
    private String modelKey;
    private String modelType;
    private Long costPoints;
    private String description;
    private Integer status;
    private Integer deleted;
    private Integer sortOrder;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
