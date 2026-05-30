// GalleryWorkResponse.java
package com.aiplatform.dto.gallery;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class GalleryWorkResponse {
    private String id;
    private String prompt;
    private String result;
    private String modelType;
    private Integer likeCount;
    private AuthorInfo author;
    private Boolean isLiked;
    private LocalDateTime createTime;

    @Data
    @Builder
    public static class AuthorInfo {
        private String userId;
        private String nickname;
    }
}