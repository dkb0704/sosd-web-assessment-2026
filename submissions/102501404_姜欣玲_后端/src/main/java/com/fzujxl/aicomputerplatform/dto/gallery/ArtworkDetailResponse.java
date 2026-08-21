package com.fzujxl.aicomputerplatform.dto.gallery;

import java.time.LocalDateTime;

public record ArtworkDetailResponse(
        Long id,
        Long userId,
        String title,
        String content,
        String status,
        Integer isPublic,
        Long viewCount,
        Long hotScore,
        LocalDateTime createTime
) {

}
