package com.example.aigenlease.service.impl;

import com.example.aigenlease.common.ApiResponse;
import com.example.aigenlease.common.PageResponse;
import com.example.aigenlease.dto.gallery.response.*;
import com.example.aigenlease.service.GalleryService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GalleryServiceImpl implements GalleryService {

        @Override
        public ApiResponse<PageResponse<GalleryWorkResponse>> getPublicWorks(
                int page,
                int size,
                String category,
                String sort
        ) {
        List<GalleryWorkResponse> records = List.of(
                new GalleryWorkResponse(
                        1L,
                        1001L,
                        "testUser",
                        "赛博城市",
                        "A futuristic cyberpunk city at night",
                        "https://example.com/work-1.png",
                        "IMAGE",
                        128,
                        1024,
                        LocalDateTime.now()
                )
        );

        PageResponse<GalleryWorkResponse> response =
                new PageResponse<>(records, page, size, 1);

        return ApiResponse.success("获取作品列表成功", response);
        }

        @Override
        public ApiResponse<GalleryWorkDetailResponse> getWorkDetail(Long workId) {

        GalleryWorkDetailResponse response =
                new GalleryWorkDetailResponse(
                        workId,
                        1001L,
                        "testUser",
                        "赛博朋克城市",
                        "A futuristic cyberpunk city at night...",
                        "https://example.com/work.png",
                        "IMAGE",
                        120,
                        5000,
                        false,
                        "SDXL",
                        20,
                        LocalDateTime.now()
                );

        return ApiResponse.success("获取作品详情成功", response);
        }

        @Override
        public ApiResponse<Void> likeWork(Long workId, Long userId) {
        return ApiResponse.success("点赞成功", null);
        }

        @Override
        public ApiResponse<Void> unlikeWork(Long workId, Long userId) {
        return ApiResponse.success("取消点赞成功", null);
        }
}