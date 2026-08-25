package com.aiplatform.service.gallery;

import com.aiplatform.dto.gallery.CommentResponse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface CommentService {
    CommentResponse addComment(Long workId, Long userId, String content);
    void deleteComment(Long workId, Long commentId, Long userId);
    // CommentService
    Page<CommentResponse> listComments(Long workId, int page, int size, Long currentUserId);
}