package com.example.aigenlease.controller;

import com.example.aigenlease.common.ApiResponse;
import com.example.aigenlease.common.PageResponse;
import com.example.aigenlease.dto.gallery.response.*;
import com.example.aigenlease.service.GalleryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/gallery")
public class GalleryController {

    @Autowired
    private GalleryService galleryService;

    @GetMapping("/works")
    public ApiResponse<PageResponse<GalleryWorkResponse>> getPublicWorks(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "hot") String sort
    ) {
        return galleryService.getPublicWorks(page, size, category, sort);
    }

    @GetMapping("/works/{workId}")
    public ApiResponse<GalleryWorkDetailResponse> getWorkDetail(
            @PathVariable Long workId
    ) {
        return galleryService.getWorkDetail(workId);
    }

    @PostMapping("/works/{workId}/like")
    public ApiResponse<Void> likeWork(
            @PathVariable Long workId,
            @RequestAttribute("userId") Long userId
    ) {
        return galleryService.likeWork(workId, userId);
    }

    @DeleteMapping("/works/{workId}/like")
    public ApiResponse<Void> unlikeWork(
            @PathVariable Long workId,
            @RequestAttribute("userId") Long userId
    ) {
        return galleryService.unlikeWork(workId, userId);
    }
}