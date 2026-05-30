package com.aiplatform.service.gallery;

import com.aiplatform.dto.gallery.GalleryWorkResponse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface GalleryService {
    Page<GalleryWorkResponse> getPublicWorks(int page, int size, String sort, String category);
    GalleryWorkResponse getWorkDetail(Long workId, Long currentUserId);
    int likeWork(Long workId, Long userId);
    int unlikeWork(Long workId, Long userId);
}