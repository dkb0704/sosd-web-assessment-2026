package com.fzujxl.aicomputerplatform.service.gallery;

import com.fzujxl.aicomputerplatform.dto.PageResultResponse;
import com.fzujxl.aicomputerplatform.dto.gallery.ArtworkDetailResponse;
import com.fzujxl.aicomputerplatform.dto.gallery.ArtworkQueryRequest;
import com.fzujxl.aicomputerplatform.dto.gallery.GalleryItemResponse;
import jakarta.validation.Valid;

public interface GalleryService {
    PageResultResponse<GalleryItemResponse> getArtWorksList(@Valid ArtworkQueryRequest request);

    ArtworkDetailResponse getArtWorkDetail(Long id, String ip);
}
