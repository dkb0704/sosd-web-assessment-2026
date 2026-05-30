package com.aiplatform.controller.gallery;

import com.aiplatform.common.Result;
import com.aiplatform.dto.gallery.CommentResponse;
import com.aiplatform.service.gallery.CommentService;
import com.aiplatform.service.gallery.GalleryService;
import com.aiplatform.util.JwtUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/gallery")
@RequiredArgsConstructor
public class GalleryController {

    private final GalleryService galleryService;
    private final CommentService commentService;
    private final JwtUtil jwtUtil;   // 新增

    // 从请求头中获取当前登录的用户ID，未登录或token无效返回null
    private Long getUserIdFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (jwtUtil.validateToken(token)) {
                return jwtUtil.getUserIdFromToken(token);
            }
        }
        return null;
    }

    @GetMapping("/works")
    public Result<?> listWorks(@RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "20") int size,
                               @RequestParam(defaultValue = "latest") String sort,
                               @RequestParam(required = false) String category) {
        return Result.success(galleryService.getPublicWorks(page, size, sort, category));
    }

    @GetMapping("/works/{workId}")
    public Result<?> getWork(@PathVariable Long workId, HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);   // 可选登录
        return Result.success(galleryService.getWorkDetail(workId, userId));
    }

    @PostMapping("/works/{workId}/like")
    public Result<?> like(@PathVariable Long workId, HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) return Result.unauthorized("请登录后点赞");
        int likeCount = galleryService.likeWork(workId, userId);
        return Result.success(likeCount);
    }

    @DeleteMapping("/works/{workId}/like")
    public Result<?> unlike(@PathVariable Long workId, HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) return Result.unauthorized("请登录后取消点赞");
        int likeCount = galleryService.unlikeWork(workId, userId);
        return Result.success(likeCount);
    }

    @PostMapping("/works/{workId}/comments")
    public Result<?> addComment(@PathVariable Long workId,
                                @RequestBody Map<String, String> body,
                                HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) return Result.unauthorized("请登录后评论");
        String content = body.get("content");
        CommentResponse response = commentService.addComment(workId, userId, content);
        return Result.success(response);
    }

    @DeleteMapping("/works/{workId}/comments/{commentId}")
    public Result<?> deleteComment(@PathVariable Long workId,
                                   @PathVariable Long commentId,
                                   HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) return Result.unauthorized("请登录后删除评论");
        commentService.deleteComment(workId, commentId, userId);
        return Result.success();
    }

    @GetMapping("/works/{workId}/comments")
    public Result<?> listComments(@PathVariable Long workId,
                                  @RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "20") int size,
                                  HttpServletRequest request) {
        Long userId = getUserIdFromToken(request); // 从 JWT 中拿到当前用户 ID（未登录则为 null）
        Page<CommentResponse> result = commentService.listComments(workId, page, size, userId);
        return Result.success(result);
    }
}