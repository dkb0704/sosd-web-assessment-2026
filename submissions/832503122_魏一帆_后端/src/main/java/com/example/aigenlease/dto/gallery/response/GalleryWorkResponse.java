package com.example.aigenlease.dto.gallery.response;

import java.time.LocalDateTime;

public record GalleryWorkResponse(
        Long workId,
        Long userId,
        String username,
        String title,
        String prompt,
        String resultUrl,
        String category,
        Integer likeCount,
        Integer viewCount,
        LocalDateTime createdAt
) {
}