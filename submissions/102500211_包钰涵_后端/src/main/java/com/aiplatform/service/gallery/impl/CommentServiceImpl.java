package com.aiplatform.service.gallery.impl;

import com.aiplatform.common.BusinessException;
import com.aiplatform.dto.gallery.CommentResponse;
import com.aiplatform.entity.Comment;
import com.aiplatform.entity.User;
import com.aiplatform.mapper.CommentMapper;
import com.aiplatform.mapper.UserMapper;
import com.aiplatform.service.gallery.CommentService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public CommentResponse addComment(Long workId, Long userId, String content) {
        if (content == null || content.trim().length() == 0) {
            throw new BusinessException(400, "评论内容不能为空");
        }
        if (content.length() > 200) {
            throw new BusinessException(400, "评论内容不能超过200字");
        }
        Comment comment = new Comment();
        comment.setWorkId(workId);
        comment.setUserId(userId);
        comment.setContent(content.trim());
        commentMapper.insert(comment);

        User user = userMapper.selectById(userId);
        return buildResponse(comment, user,userId);
    }

    @Override
    @Transactional
    public void deleteComment(Long workId, Long commentId, Long userId) {
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null || !comment.getWorkId().equals(workId)) {
            throw new BusinessException(404, "评论不存在");
        }
        if (!comment.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权删除他人评论");
        }
        commentMapper.deleteById(commentId);
    }

    @Override
    public Page<CommentResponse> listComments(Long workId, int page, int size,Long currentUserId) {
        Page<Comment> commentPage = commentMapper.selectPage(
                new Page<>(page, size),
                new LambdaQueryWrapper<Comment>()
                        .eq(Comment::getWorkId, workId)
                        .orderByDesc(Comment::getCreateTime)
        );
        List<CommentResponse> records = commentPage.getRecords().stream()
                .map(c -> {
                    User user = userMapper.selectById(c.getUserId());
                    return buildResponse(c, user,currentUserId);
                }).collect(Collectors.toList());
        Page<CommentResponse> result = new Page<>(page, size, commentPage.getTotal());
        result.setRecords(records);
        return result;
    }

    private CommentResponse buildResponse(Comment comment, User user,Long currentUserId) {
        CommentResponse.AuthorInfo author = CommentResponse.AuthorInfo.builder()
                .userId(String.valueOf(user.getId()))
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .build();
        return CommentResponse.builder()
                .commentId(String.valueOf(comment.getCommentId()))
                .content(comment.getContent())
                .author(author)
                .createTime(comment.getCreateTime())
                .canDelete(currentUserId != null && comment.getUserId().equals(currentUserId))  // 新增
                .build();
    }
}