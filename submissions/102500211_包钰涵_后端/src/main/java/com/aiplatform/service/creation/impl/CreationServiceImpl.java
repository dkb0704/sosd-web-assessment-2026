package com.aiplatform.service.creation.impl;

import cn.hutool.core.util.StrUtil;
import com.aiplatform.common.BusinessException;
import com.aiplatform.config.RabbitConfig;
import com.aiplatform.dto.creation.MyWorkDetailResponse;
import com.aiplatform.dto.creation.TaskDetailResponse;
import com.aiplatform.entity.*;
import com.aiplatform.mapper.*;
import com.aiplatform.service.creation.CreationService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import com.aiplatform.util.RedisUtil;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreationServiceImpl implements CreationService {

    private final CreationTaskMapper taskMapper;
    private final AiModelMapper modelMapper;
    private final UserMapper userMapper;
    private final RabbitTemplate rabbitTemplate;
    private final RedisUtil redisUtil;
    private final LikeRecordMapper likeMapper;
    private final CommentMapper commentMapper;



    @Value("${file.upload-dir:./uploads}")
    private String uploadDir;

    @Transactional
    @Override
    public Long submitTask(Long userId, Long modelId, String prompt, Boolean isPublic, MultipartFile[] files) {
        log.warn("接收到的 userId: {}", userId);
        log.error("!!! 前端传来的userId = {}", userId);
        // 1. 模型检查
        AiModel model = modelMapper.selectById(modelId);
        if (model == null || model.getStatus() != 1) {
            throw new BusinessException("模型不可用");
        }

        // 2. 算力扣减（原子）
        int cost = model.getCostPerTask();
        User user = userMapper.selectById(userId);
        log.warn("实际读到的算力点数：{}", user.getPoints());
        log.warn("准备扣减算力，userId: {}, cost: {}", userId, cost);
        log.warn("当前请求 userId: {}, 扣减点数: {}", userId, cost);
        int updated = userMapper.updatePoints(userId, -cost);
        if (updated == 0) {
            throw new BusinessException("算力不足");
        }

        // 3. 附件处理（存储）
        String fileUrls = null;
        if (files != null && files.length > 0) {
            long totalSize = 0;
            for (MultipartFile f : files) totalSize += f.getSize();
            if (totalSize > 20 * 1024 * 1024) {
                // 补偿算力
                userMapper.updatePoints(userId, cost);
                throw new BusinessException(400, "附件总大小超过20MB");
            }
            List<String> urls = new ArrayList<>();
            for (MultipartFile f : files) {
                String originalName = f.getOriginalFilename();
                String ext = "";
                if (originalName != null && originalName.contains(".")) {
                    ext = originalName.substring(originalName.lastIndexOf("."));
                }
                String fileName = UUID.randomUUID() + ext;
                try {
                    Path dir = Paths.get(uploadDir);
                    if (!Files.exists(dir)) Files.createDirectories(dir);
                    Files.copy(f.getInputStream(), dir.resolve(fileName));
                    urls.add("/uploads/" + fileName);
                } catch (IOException e) {
                    // 文件存储失败，回滚算力
                    userMapper.updatePoints(userId, cost);
                    log.error("文件上传失败", e);
                    throw new BusinessException("文件上传失败");
                }
            }
            fileUrls = String.join(",", urls);
        }

        // 4. 创建任务
        CreationTask task = new CreationTask();
        task.setUserId(userId);
        task.setPrompt(prompt);
        task.setModelId(modelId);
        task.setIsPublic(isPublic != null && isPublic ? 1 : 0);
        task.setStatus(0);
        task.setPointsCost(cost);
        task.setFileUrls(fileUrls);
        task.setSubmitTime(LocalDateTime.now());
        taskMapper.insert(task);

        // 5. 发送MQ
        rabbitTemplate.convertAndSend(RabbitConfig.TASK_QUEUE, task.getTaskId());

        return task.getTaskId();
    }

    @Override
    public TaskDetailResponse getTaskDetail(Long taskId) {
        CreationTask task = taskMapper.selectById(taskId);
        if (task == null) throw new BusinessException(404, "任务不存在");
        return buildDetail(task);
    }

    @Override
    public Page<TaskDetailResponse> getUserTasks(Long userId, int page, int size) {
        Page<CreationTask> taskPage = taskMapper.selectPage(
                new Page<>(page, size),
                new LambdaQueryWrapper<CreationTask>()
                        .eq(CreationTask::getUserId, userId)
                        .orderByDesc(CreationTask::getSubmitTime));
        Page<TaskDetailResponse> resultPage = new Page<>(page, size, taskPage.getTotal());
        List<TaskDetailResponse> list = taskPage.getRecords().stream()
                .map(this::buildDetail).toList();
        resultPage.setRecords(list);
        return resultPage;
    }

    @Override
    @Transactional
    public void updateVisibility(Long taskId, Long userId, boolean isPublic) {
        CreationTask task = taskMapper.selectById(taskId);
        if (task == null) {
            throw new BusinessException(404, "任务不存在");
        }
        if (!task.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权修改该作品的公开状态");
        }

        // ✅ 只更新 is_public 字段，不动 result、status 等其他字段
        LambdaUpdateWrapper<CreationTask> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(CreationTask::getTaskId, taskId)
                .set(CreationTask::getIsPublic, isPublic ? 1 : 0);
        taskMapper.update(null, updateWrapper);
    }

    private TaskDetailResponse buildDetail(CreationTask task) {
        return TaskDetailResponse.builder()
                .taskId(String.valueOf(task.getTaskId()))
                .status(task.getStatus())
                .result(task.getResult())
                .errorMsg(task.getErrorMsg())
                .pointsCost(task.getPointsCost())
                .submitTime(task.getSubmitTime())
                .finishTime(task.getFinishTime())
                .isPublic(task.getIsPublic() != null && task.getIsPublic() == 1)  // 新增这一行
                .build();
    }

    @Override
    public MyWorkDetailResponse getMyWorkDetail(Long workId, Long userId) {
        CreationTask task = taskMapper.selectById(workId);
        if (task == null) {
            throw new BusinessException(404, "作品不存在");
        }
        if (!task.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权查看该作品");
        }
        // 点赞数
        long likeCount = likeMapper.selectCount(
                new LambdaQueryWrapper<LikeRecord>().eq(LikeRecord::getWorkId, workId));
        // 评论数
        long commentCount = commentMapper.selectCount(
                new LambdaQueryWrapper<Comment>().eq(Comment::getWorkId, workId));

        return MyWorkDetailResponse.builder()
                .taskId(String.valueOf(task.getTaskId()))
                .status(task.getStatus())
                .result(task.getResult())
                .errorMsg(task.getErrorMsg())
                .pointsCost(task.getPointsCost())
                .isPublic(task.getIsPublic() != null && task.getIsPublic() == 1)
                .likeCount((int) likeCount)
                .commentCount((int) commentCount)
                .prompt(task.getPrompt())
                .submitTime(task.getSubmitTime())
                .finishTime(task.getFinishTime())
                .build();
    }
}