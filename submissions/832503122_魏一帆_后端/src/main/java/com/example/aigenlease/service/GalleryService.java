package com.example.aigenlease.service;

import com.example.aigenlease.common.ApiResponse;
import com.example.aigenlease.common.PageResponse;
import com.example.aigenlease.dto.gallery.response.*;

public interface GalleryService {

    ApiResponse<PageResponse<GalleryWorkResponse>> getPublicWorks(
            int page,
            int size,
            String category,
            String sort
    );
    ApiResponse<GalleryWorkDetailResponse> getWorkDetail(Long workId);
    ApiResponse<Void> likeWork(Long workId, Long userId);
    ApiResponse<Void> unlikeWork(Long workId, Long userId);
}