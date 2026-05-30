package com.aiplatform.service.gallery.impl;

import com.aiplatform.common.BusinessException;
import com.aiplatform.dto.gallery.GalleryWorkResponse;
import com.aiplatform.entity.AiModel;
import com.aiplatform.entity.CreationTask;
import com.aiplatform.entity.LikeRecord;
import com.aiplatform.entity.User;
import com.aiplatform.mapper.AiModelMapper;
import com.aiplatform.mapper.CreationTaskMapper;
import com.aiplatform.mapper.LikeRecordMapper;
import com.aiplatform.mapper.UserMapper;
import com.aiplatform.service.gallery.GalleryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GalleryServiceImpl implements GalleryService {

    private final CreationTaskMapper taskMapper;
    private final LikeRecordMapper likeMapper;
    private final UserMapper userMapper;
    private final AiModelMapper modelMapper;

    @Override
    public Page<GalleryWorkResponse> getPublicWorks(int page, int size, String sort, String category) {
        LambdaQueryWrapper<CreationTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CreationTask::getIsPublic, 1)
                .eq(CreationTask::getStatus, 1); // 只展示已生成成功的

        if ("text".equals(category)) {
            wrapper.eq(CreationTask::getModelId, 1L); // 假设模型1为text，实际需关联查询
        } else if ("image".equals(category)) {
            wrapper.eq(CreationTask::getModelId, 2L); // 模型2为image
        }

        if ("hot".equals(sort)) {
            wrapper.orderByDesc(CreationTask::getLikeCount);
        } else {
            wrapper.orderByDesc(CreationTask::getSubmitTime);
        }

        Page<CreationTask> taskPage = taskMapper.selectPage(new Page<>(page, size), wrapper);
        List<GalleryWorkResponse> records = taskPage.getRecords().stream()
                .map(task -> buildResponse(task, null)).collect(Collectors.toList());

        Page<GalleryWorkResponse> resultPage = new Page<>(page, size, taskPage.getTotal());
        resultPage.setRecords(records);
        return resultPage;
    }

    @Override
    public GalleryWorkResponse getWorkDetail(Long workId, Long currentUserId) {
        CreationTask task = taskMapper.selectById(workId);
        if (task == null || task.getIsPublic() != 1) throw new BusinessException(404, "作品不存在");
        return buildResponse(task, currentUserId);
    }

    @Transactional
    @Override
    public int likeWork(Long workId, Long userId) {
        CreationTask task = taskMapper.selectById(workId);
        if (task == null || task.getIsPublic() != 1) throw new BusinessException(404, "作品不存在");

        Long count = likeMapper.selectCount(new LambdaQueryWrapper<LikeRecord>()
                .eq(LikeRecord::getWorkId, workId).eq(LikeRecord::getUserId, userId));
        if (count > 0) throw new BusinessException("已点赞，不能重复点赞");

        LikeRecord record = new LikeRecord();
        record.setWorkId(workId);
        record.setUserId(userId);
        likeMapper.insert(record);

        long totalLikes = likeMapper.selectCount(new LambdaQueryWrapper<LikeRecord>()
                .eq(LikeRecord::getWorkId, workId));

        // ✅ 直接用列名字符串，绕过实体映射
        LambdaUpdateWrapper<CreationTask> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(CreationTask::getTaskId, workId)
                .setSql("like_count = " + totalLikes); // 直接用SQL片段
        taskMapper.update(null, updateWrapper);

        return (int) totalLikes;
    }

    @Transactional
    @Override
    public int unlikeWork(Long workId, Long userId) {
        likeMapper.delete(new LambdaQueryWrapper<LikeRecord>()
                .eq(LikeRecord::getWorkId, workId).eq(LikeRecord::getUserId, userId));
        long totalLikes = likeMapper.selectCount(new LambdaQueryWrapper<LikeRecord>()
                .eq(LikeRecord::getWorkId, workId));

        LambdaUpdateWrapper<CreationTask> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(CreationTask::getTaskId, workId)
                .setSql("like_count = " + totalLikes); // 直接用SQL片段
        taskMapper.update(null, updateWrapper);

        return (int) totalLikes;
    }

    private GalleryWorkResponse buildResponse(CreationTask task, Long currentUserId) {
        // 查询模型类型
        AiModel model = modelMapper.selectById(task.getModelId());
        String modelType = model != null ? model.getModelKey() : null;

        // 查询作者
        User author = userMapper.selectById(task.getUserId());
        String nickname = (author != null && author.getNickname() != null) ? author.getNickname() : "未知用户";

        // 实时查询点赞数
        long likeCount = likeMapper.selectCount(new LambdaQueryWrapper<LikeRecord>()
                .eq(LikeRecord::getWorkId, task.getTaskId()));

        Boolean isLiked = false;
        if (currentUserId != null) {
            isLiked = likeMapper.selectCount(new LambdaQueryWrapper<LikeRecord>()
                    .eq(LikeRecord::getWorkId, task.getTaskId())
                    .eq(LikeRecord::getUserId, currentUserId)) > 0;
        }

        GalleryWorkResponse.AuthorInfo authorInfo = GalleryWorkResponse.AuthorInfo.builder()
                .userId(String.valueOf(task.getUserId()))
                .nickname(nickname)
                .build();

        return GalleryWorkResponse.builder()
                .id(String.valueOf(task.getTaskId()))
                .prompt(task.getPrompt())
                .result(task.getResult())
                .modelType(modelType)
                .likeCount((int) likeCount)   // 用实时查询的值
                .author(authorInfo)
                .isLiked(isLiked)
                .createTime(task.getSubmitTime())
                .build();
    }
}