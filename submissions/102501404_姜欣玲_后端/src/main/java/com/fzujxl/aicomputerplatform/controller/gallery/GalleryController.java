package com.fzujxl.aicomputerplatform.controller.gallery;

import com.fzujxl.aicomputerplatform.common.Result;
import com.fzujxl.aicomputerplatform.dto.PageResultResponse;
import com.fzujxl.aicomputerplatform.dto.gallery.ArtworkDetailResponse;
import com.fzujxl.aicomputerplatform.dto.gallery.ArtworkQueryRequest;
import com.fzujxl.aicomputerplatform.dto.gallery.GalleryItemResponse;
import com.fzujxl.aicomputerplatform.service.gallery.GalleryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/gallery")
public class GalleryController {

    private final GalleryService galleryService;

    public GalleryController(GalleryService galleryService) {
        this.galleryService = galleryService;
    }

    @GetMapping("/artworks")
    public Result<PageResultResponse<GalleryItemResponse>> getArtWorks(@Valid @RequestBody ArtworkQueryRequest artworkQueryRequest) {
        log.info("收到查询请求： {}", artworkQueryRequest);
        PageResultResponse<GalleryItemResponse> response = galleryService.getArtWorksList(artworkQueryRequest);
        return Result.success("查询成功", response);
    }

    @GetMapping("/artwork/{id}")
    public Result<ArtworkDetailResponse> getArtWorkDetail(@PathVariable Long id, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        log.info("收到查询请求：id {} ip {}", id, ip);
        ArtworkDetailResponse response = galleryService.getArtWorkDetail(id, ip);
        return Result.success("查询成功", response);
    }
}
