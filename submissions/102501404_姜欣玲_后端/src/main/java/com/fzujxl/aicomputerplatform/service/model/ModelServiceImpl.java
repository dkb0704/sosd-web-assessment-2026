package com.fzujxl.aicomputerplatform.service.model;

import cn.hutool.core.bean.BeanUtil;
import com.fzujxl.aicomputerplatform.common.ResultCode;
import com.fzujxl.aicomputerplatform.dto.model.*;
import com.fzujxl.aicomputerplatform.entity.Model;
import com.fzujxl.aicomputerplatform.exception.BusinessException;
import com.fzujxl.aicomputerplatform.mapper.model.ModelMapper;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ModelServiceImpl implements ModelService {

    private final RedissonClient redissonClient;
    private final ModelMapper modelMapper;

    public ModelServiceImpl(RedissonClient redissonClient, ModelMapper modelMapper) {
        this.redissonClient = redissonClient;
        this.modelMapper = modelMapper;
    }
    @Override
    public AddModelResponse addModel(AddModelRequest request) {
        log.info("添加模型请求:{}", request);
        String lockKey = "addModel"+request.getModelKey();
        RLock lock = redissonClient.getLock(lockKey);
        boolean isLock = false;
        try {
            isLock = lock.tryLock(1,10, TimeUnit.SECONDS);
            if(!isLock){
                throw new BusinessException(ResultCode.INTERNAL_ERROR, "操作太频繁,请稍后重试");
            }
            Model existModel = modelMapper.selectByModelKey(request.getModelKey());
            if (existModel !=null){
                throw new BusinessException(ResultCode.BAD_REQUEST,"模型已存在");
            }
            Model model = new Model();
            model.setModelKey(request.getModelKey());
            model.setModelType(request.getModelType());
            model.setDescription(request.getDescription());
            model.setStatus(request.getStatus());
            model.setSortOrder(request.getSortOrder());
            model.setCreatedTime(LocalDateTime.now());
            model.setUpdatedTime(LocalDateTime.now());
            model.setCostPoints(request.getCostPoints());
            model.setDeleted(0);

            int result = modelMapper.insert(model);
            if (result <= 0) {
                log.error("添加模型失败,模型key:{}", request.getModelKey());
                throw new BusinessException(ResultCode.INTERNAL_ERROR, "添加模型失败,请稍后重试");
            }
            return BeanUtil.copyProperties(model,AddModelResponse.class);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("锁等待被中断：{}", request.getModelKey(),e);
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "锁等待被中断");
        } finally{
            if(isLock &&lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }
    }
    @Override
    public ChangeModelStatusResponse publishModel(PublishModelRequest request) {
        log.info("上架模型请求:{}", request);
        Model existModel = modelMapper.selectByModelKey(request.getModelKey());
        isExisted(existModel);
        if (existModel.getStatus().equals(request.getStatus())){
            Model model = new Model();
            model.setId(existModel.getId());
            model.setStatus(existModel.getStatus());
            model.setUpdatedTime(LocalDateTime.now());
            return BeanUtil.copyProperties(model, ChangeModelStatusResponse.class);
        }
        int result = modelMapper.updateStatusById(existModel.getId(), request.getStatus());
        if (result <= 0) {
            log.error("模型上架失败或者超值套餐已上架,模型key:{}", request.getModelKey());
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "模型上架失败或者超值套餐已上架,请稍后重试");
        }
        Model model = new Model();
        model.setId(existModel.getId());
        model.setStatus(request.getStatus());
        return new ChangeModelStatusResponse(model.getId(),model.getStatus(),LocalDateTime.now());
    }
    @Override
    public ChangeModelStatusResponse offlineModel(OfflineModelRequest request) {
        log.info("下架模型请求:{}", request);
        Model existModel = modelMapper.selectByModelKey(request.getModelKey());
        isExisted(existModel);
        if (existModel.getStatus().equals(request.getStatus())){
            Model model = new Model();
            model.setId(existModel.getId());
            model.setStatus(existModel.getStatus());
            return BeanUtil.copyProperties(model, ChangeModelStatusResponse.class);
        }
        int result = modelMapper.updateStatusById(existModel.getId(), request.getStatus());
        if (result <= 0) {
            log.error("模型下架失败或者模型已下架,模型key:{}", request.getModelKey());
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "模型下架失败或者模型已下架,请稍后重试");
        }
        Model model = new Model();
        model.setId(existModel.getId());
        model.setStatus(request.getStatus());
        model.setUpdatedTime(LocalDateTime.now());
        return new ChangeModelStatusResponse(model.getId(),model.getStatus(),LocalDateTime.now());
    }

    @Override
    public ChangeCostPointsResponse changeCostPoints(ChangeCostPointsRequest request) {
        log.info("修改模型成本积分请求:{}", request);
        Model existModel = modelMapper.selectByModelKey(request.getModelKey());
        isExisted(existModel);
        if (existModel.getCostPoints().equals(request.getCostPoints())){
            Model model = new Model();
            model.setId(existModel.getId());
            model.setCostPoints(existModel.getCostPoints());
            model.setUpdatedTime(LocalDateTime.now());
            return BeanUtil.copyProperties(model, ChangeCostPointsResponse.class);
        }
        int result = modelMapper.updateCostPointsById(existModel.getId(), request.getCostPoints(), existModel.getCostPoints());
        if (result <= 0) {
            log.error("修改模型成本积分失败,模型key:{}", request.getModelKey());
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "修改模型成本积分失败,请稍后重试");
        }
        Model model = new Model();
        model.setId(existModel.getId());
        model.setCostPoints(request.getCostPoints());
        model.setUpdatedTime(LocalDateTime.now());
        return new ChangeCostPointsResponse(model.getId(),model.getCostPoints(),LocalDateTime.now());
    }

    @Override
    public void deleteModel(DeleteModelRequest request) {
        log.info("删除模型请求:{}", request);
        Model existModel = modelMapper.selectByModelKey(request.getModelKey());
        isExisted(existModel);
        int result = modelMapper.deleteById(existModel.getId());
        if (result <= 0) {
            log.error("删除模型失败或者模型已删除,模型key:{}", request.getModelKey());
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "删除模型失败或者模型已删除,请稍后重试");
        }
    }

    private void isExisted(Model existModel) {
        if (existModel ==null|| existModel.getDeleted()!=0) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "模型不存在");
        }
    }
}
