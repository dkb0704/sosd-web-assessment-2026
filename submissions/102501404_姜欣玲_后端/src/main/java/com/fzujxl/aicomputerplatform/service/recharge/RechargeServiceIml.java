package com.fzujxl.aicomputerplatform.service.recharge;

import cn.hutool.core.bean.BeanUtil;
import com.fzujxl.aicomputerplatform.common.ResultCode;
import com.fzujxl.aicomputerplatform.dto.rechage.*;
import com.fzujxl.aicomputerplatform.entity.RechargePackage;
import com.fzujxl.aicomputerplatform.exception.BusinessException;
import com.fzujxl.aicomputerplatform.mapper.recharge.RechargeMapper;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RechargeServiceIml implements RechargeService {

    public final RechargeMapper RechargeMapper;
    public final RedissonClient redissonClient;

    public RechargeServiceIml(RechargeMapper RechargeMapper, RedissonClient redissonClient) {
        this.RechargeMapper = RechargeMapper;
        this.redissonClient = redissonClient;
    }

    @Override
    public AddRechargePackageResponse addRechargePackage(AddRechargePackageRequest request){
        log.info("添加充值套餐请求:{}", request);
        String lockKey = "addRechargePackage"+request.getPackageName();
        RLock lock = redissonClient.getLock(lockKey);
        boolean isLock = false;
        try {
            isLock = lock.tryLock(1,10, TimeUnit.SECONDS);
            if(!isLock){
                throw new BusinessException(ResultCode.INTERNAL_ERROR, "操作太频繁,请稍后重试");
            }
            RechargePackage existPack = RechargeMapper.selectByPackageName(request.getPackageName());
            if (existPack!=null){
                throw new BusinessException(ResultCode.BAD_REQUEST,"充值套餐已存在");
            }
            RechargePackage pack = new RechargePackage();
            pack.setPackageName(request.getPackageName());
            pack.setPackageType(request.getPackageType());
            pack.setOriginalPrice(request.getOriginalPrice());
            pack.setCurrentPrice(request.getCurrentPrice());
            pack.setDuration(request.getDuration());
            pack.setStatus(request.getStatus());
            pack.setSortOrder(request.getSortOrder());
            pack.setCreatedTime(LocalDateTime.now());
            pack.setUpdatedTime(LocalDateTime.now());
            pack.setPoints(request.getPoints());
            pack.setDailyPoints(request.getDailyPoints());
            pack.setDeleted(0);
                int result = RechargeMapper.insert(pack);
                if (result <= 0) {
                    log.error("添加充值套餐失败,套餐名称:{}", request.getPackageName());
                    throw new BusinessException(ResultCode.INTERNAL_ERROR, "添加充值套餐失败,请稍后重试");
                }
            return BeanUtil.copyProperties(pack, AddRechargePackageResponse.class);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("锁等待被中断：{}", request.getPackageName(),e);
                throw new BusinessException(ResultCode.INTERNAL_ERROR, "锁等待被中断");
            } finally{
                if(isLock &&lock.isHeldByCurrentThread()){
                    lock.unlock();
                }
            }
    }

    @Override
    public ChangeRechargePackageStatusResponse publishRechargePackage(PublishRechargePackageRequest request) {
        log.info("上架充值套餐请求:{}", request);
        RechargePackage existPack = RechargeMapper.selectByPackageName(request.getPackageName());
        isExisted(existPack);
        if (existPack.getStatus().equals(request.getStatus())){
                RechargePackage pack = new RechargePackage();
                pack.setId(existPack.getId());
                pack.setStatus(existPack.getStatus());
                return BeanUtil.copyProperties(pack, ChangeRechargePackageStatusResponse.class);
            }
            int result = RechargeMapper.updateByPackageName(request.getPackageName(),request.getStatus());
            if (result <= 0) {
                log.error("充值套餐上架失败或者超值套餐已上架,套餐名称:{}", request.getPackageName());
                throw new BusinessException(ResultCode.INTERNAL_ERROR, "充值套餐上架失败或者超值套餐已上架,请稍后重试");
            }
            RechargePackage pack = new RechargePackage();
            pack.setId(existPack.getId());
            pack.setStatus(request.getStatus());
            return new ChangeRechargePackageStatusResponse(pack.getStatus(),pack.getId(),LocalDateTime.now());
        }

    @Override
    public ChangeRechargePackageStatusResponse offlineRechargePackage(OfflineRechargePackageRequest request) {
        log.info("下架充值套餐请求:{}", request);
        RechargePackage existPack = RechargeMapper.selectByPackageName(request.getPackageName());
        isExisted(existPack);
        if (isEquals(request, existPack)){
            RechargePackage pack = new RechargePackage();
            pack.setId(existPack.getId());
            pack.setStatus(existPack.getStatus());
            return BeanUtil.copyProperties(pack, ChangeRechargePackageStatusResponse.class);
        }
        int result = RechargeMapper.updateByPackageName(request.getPackageName(),request.getStatus());
        if (result <= 0) {
            log.error("充值套餐下架失败或者超值套餐已下架,套餐名称:{}", request.getPackageName());
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "充值套餐下架失败或者超值套餐已下架,请稍后重试");
        }
        RechargePackage pack = new RechargePackage();
        pack.setId(existPack.getId());
        pack.setStatus(request.getStatus());
        return new ChangeRechargePackageStatusResponse(pack.getStatus(),pack.getId(),LocalDateTime.now());
    }

    private boolean isEquals(OfflineRechargePackageRequest request, RechargePackage existPack) {
        return existPack.getStatus().equals(request.getStatus());
    }

    private void isExisted(RechargePackage existPack) {
        if (existPack ==null|| existPack.getDeleted()!=0) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "充值套餐不存在");
        }
    }
}
    