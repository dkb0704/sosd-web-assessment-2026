package com.example.computingpowerrental.service.impl;

import com.example.computingpowerrental.entity.AiModel;
import com.example.computingpowerrental.entity.AiTask;
import com.example.computingpowerrental.entity.AiTaskMessage;
import com.example.computingpowerrental.enums.AiModelStatus;
import com.example.computingpowerrental.enums.AiTaskStatus;
import com.example.computingpowerrental.mapper.AiModelMapper;
import com.example.computingpowerrental.mapper.AiTaskMapper;
import com.example.computingpowerrental.service.AiTaskMessageProducer;
import com.example.computingpowerrental.service.AiTaskService;
import com.example.computingpowerrental.service.ComputePointService;
import com.example.computingpowerrental.vo.TaskPageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

/**
 * @author Lark
 * @ date 2026/8/6  18:10
 * @ description AI任务业务实现类（提交AI生成任务，查询任务，更新任务状态）
 */
@Service
public class AiTaskServiceImpl implements AiTaskService{
    @Autowired
    private ComputePointService computePointService;

    @Autowired
    private AiTaskMessageProducer aiTaskMessageProducer;

    private final AiTaskMapper aiTaskMapper;

    private final AiModelMapper aiModelMapper;

    public AiTaskServiceImpl(AiTaskMapper aiTaskMapper,AiModelMapper aiModelMapper) {
        this.aiTaskMapper = aiTaskMapper;
        this.aiModelMapper = aiModelMapper;
    }

    //提交AI任务
    @Override
    @Transactional
    public AiTask submitTask(Long userId,String prompt,Long modelId,String category,Boolean isPublic) {
        //查询模型
        AiModel model = aiModelMapper.findById(modelId);

        if (model == null) {
            throw new RuntimeException(
                    "模型不存在"
            );

        }

        //判断模型是否可用
        if (!AiModelStatus.PUBLISHED.getCode().equals(model.getStatus())) {
            throw new RuntimeException("当前模型不可用");
        }

        //获取本次调用需要消耗的算力
        Integer costPoints = model.getCostPoints();

        //通过Redis原子校验并扣减算力
        boolean consumed =
                computePointService.consumePoints(
                        userId,
                        costPoints
                );

        if (!consumed) {
            throw new RuntimeException("算力不足");
        }

        //创建AI任务
        AiTask task = new AiTask();

        task.setUserId(userId);

        task.setPrompt(prompt);

        task.setModelId(model.getId());

        //保存模型名称快照
        task.setModelName(model.getModelName());

        task.setCategory(category);

        task.setCostPoints(costPoints);

        //初始状态：WAITING等待生成
        task.setStatus(AiTaskStatus.WAITING.getCode());

        task.setIsPublic(isPublic != null && isPublic);

        task.setLikeCount(0);

        int insert = aiTaskMapper.insert(task);

        if (insert == 0) {
            throw new RuntimeException("创建任务失败");
        }

        /*
         * 等AI任务真正写入数据库以后再发送MQ消息，
         * 防止消费者先于数据库事务提交执行。
         */
        if (TransactionSynchronizationManager.isSynchronizationActive()) {

            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            aiTaskMessageProducer.send(
                                    new AiTaskMessage(task.getId())
                            );
                        }
                    }
            );

        } else {
            aiTaskMessageProducer.send(
                    new AiTaskMessage(task.getId())
            );
        }

        return task;
    }

    //根据ID查询任务
    @Override
    public AiTask getTaskById(Long id,Long userId) {
        AiTask task = aiTaskMapper.findByIdAndUserId(id,userId);
        if (task == null) {
            throw new RuntimeException("任务不存在");
        }
        return task;
    }

    //查询用户任务
    @Override
    public List<AiTask> listUserTasks(Long userId) {
        return aiTaskMapper.findByUserId(userId);
    }

    //任务成功
    @Override
    public void success(Long taskId, String result) {
        int update = aiTaskMapper.updateSuccess(
                taskId,
                AiTaskStatus.PROCESSING.getCode(),
                AiTaskStatus.SUCCESS.getCode(),
                result
        );

        if(update == 0){
            throw new RuntimeException("更新任务结果失败");
        }
    }

    //任务失败
    @Override
    public void failed(Long taskId, String errorMessage) {
        aiTaskMapper.updateFailed(
                taskId,
                AiTaskStatus.PROCESSING.getCode(),
                AiTaskStatus.FAILED.getCode(),
                errorMessage
        );
    }

    @Override
    public TaskPageVO pageTasks(Long userId, Integer page, Integer size, Integer status){
        if (page == null || page < 1) {
            page = 1;
        }

        if (size == null || size < 1) {
            size = 10;
        }

        if (size > 100) {
            size = 100;
        }

        int offset = (page - 1) * size;

        List<AiTask> records =aiTaskMapper.findPageByUserId(userId, status, offset, size);

        Long total = aiTaskMapper.countByUserId(userId, status);

        TaskPageVO vo = new TaskPageVO();

        vo.setRecords(records);

        vo.setPage(page);

        vo.setSize(size);

        vo.setTotal(total);


        return vo;
    }

    //管理端分页查询全站AI任务
    @Override
    public TaskPageVO adminPageTasks(Integer page, Integer size, Integer status) {

        if (page == null || page < 1) {
            page = 1;
        }

        if (size == null || size < 1) {
            size = 10;
        }

        if (size > 100) {
            size = 100;
        }

        int offset = (page - 1) * size;

        List<AiTask> records = aiTaskMapper.findPage(status, offset, size);

        Long total = aiTaskMapper.countAll(status);

        TaskPageVO vo = new TaskPageVO();

        vo.setRecords(records);
        vo.setPage(page);
        vo.setSize(size);
        vo.setTotal(total);

        return vo;
    }

    @Override
    public TaskPageVO gallery(String category, String sort, Integer page, Integer size){

        if (page == null || page < 1) {
            page = 1;
        }

        if (size == null || size < 1) {
            size = 10;
        }

        if (size > 100) {
            size = 100;
        }

        int offset = (page - 1) * size;

        List<AiTask> records = aiTaskMapper.findPublicTasks(category, sort, offset, size);

        Long total = aiTaskMapper.countPublicTasks(category);

        TaskPageVO vo = new TaskPageVO();

        vo.setRecords(records);

        vo.setPage(page);

        vo.setSize(size);

        vo.setTotal(total);

        return vo;
    }

    @Override
    public void updatePublicStatus(Long taskId, Long userId, Boolean isPublic) {

        AiTask task = aiTaskMapper.findByIdAndUserId(taskId, userId);

        if (task == null) {
            throw new RuntimeException("任务不存在或无权操作");
        }

        if (!AiTaskStatus.SUCCESS.getCode().equals(task.getStatus())) {
            throw new RuntimeException("只有生成成功的任务才能公开");
        }

        int update = aiTaskMapper.updatePublicStatus(taskId, userId, isPublic);

        if (update == 0) {
            throw new RuntimeException("修改任务公开状态失败");
        }
    }

    @Override
    public void likeTask(Long taskId) {

        int update = aiTaskMapper.increaseLikeCount(taskId);

        if (update == 0) {
            throw new RuntimeException("作品不存在或当前不可点赞");
        }
    }
}
