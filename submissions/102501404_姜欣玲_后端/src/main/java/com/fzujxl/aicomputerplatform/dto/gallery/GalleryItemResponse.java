package com.fzujxl.aicomputerplatform.dto.gallery;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record GalleryItemResponse (
        Long id,
        String title,
        String author,
        String coverUrl,
        String description,
        String category,
        String taskType,
        String status,
        Integer isPublic,
        Long hot,
        Integer viewCount,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt

) {}
