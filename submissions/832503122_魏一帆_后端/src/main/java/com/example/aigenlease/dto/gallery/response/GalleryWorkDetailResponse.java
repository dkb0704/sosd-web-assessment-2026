package com.example.aigenlease.dto.gallery.response;

import java.time.LocalDateTime;

public record GalleryWorkDetailResponse(
        Long workId,
        Long userId,
        String username,
        String title,
        String prompt,
        String resultUrl,
        String category,
        Integer likeCount,
        Integer viewCount,
        Boolean likedByCurrentUser,
        String modelName,
        Integer costPoints,
        LocalDateTime createdAt
) {
}