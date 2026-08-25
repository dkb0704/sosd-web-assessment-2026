package com.aiplatform.dto.gallery;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class CommentResponse {
    private String commentId;
    private String content;
    private AuthorInfo author;
    private LocalDateTime createTime;
    private Boolean canDelete;   // 新增

    @Data
    @Builder
    public static class AuthorInfo {
        private String userId;
        private String nickname;
        private String avatar;
    }
}