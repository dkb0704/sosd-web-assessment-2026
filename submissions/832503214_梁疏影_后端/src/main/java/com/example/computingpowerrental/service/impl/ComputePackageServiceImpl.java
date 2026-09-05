package com.example.computingpowerrental.service.impl;

import com.example.computingpowerrental.entity.ComputePackage;
import com.example.computingpowerrental.enums.ComputePackageStatus;
import com.example.computingpowerrental.mapper.ComputePackageMapper;
import com.example.computingpowerrental.service.ComputePackageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Lark
 * @ date 2026/8/15  16:37
 * @ description 算力充值套餐业务实现类
 */
@Service
public class ComputePackageServiceImpl implements ComputePackageService{
    private final ComputePackageMapper computePackageMapper;

    public ComputePackageServiceImpl(ComputePackageMapper computePackageMapper) {
        this.computePackageMapper = computePackageMapper;
    }

    //根据ID查询套餐
    @Override
    public ComputePackage getById(Long id) {

        ComputePackage computePackage = computePackageMapper.findById(id);

        if (computePackage == null) {
            throw new RuntimeException("充值套餐不存在");
        }
        return computePackage;
    }

    //查询全部套餐（管理端）
    @Override
    public List<ComputePackage> listAll() {
        return computePackageMapper.findAll();
    }

    //查询已上架套餐（用户端）
    @Override
    public List<ComputePackage> listOnShelf() {
        return computePackageMapper.findByStatus(ComputePackageStatus.ON_SHELF.getCode());
    }

    //新增套餐
    @Override
    @Transactional
    public void addPackage(ComputePackage computePackage) {

        validatePackage(computePackage);

        //新增套餐默认处于下架状态
        computePackage.setStatus(ComputePackageStatus.OFF_SHELF.getCode());

        int rows = computePackageMapper.insert(computePackage);

        if (rows == 0) {
            throw new RuntimeException("新增充值套餐失败");
        }
    }

    //修改套餐
    @Override
    @Transactional
    public void updatePackage(ComputePackage computePackage) {

        ComputePackage exist = getById(computePackage.getId());

        validatePackage(computePackage);

        //修改基础信息时保持原来的上下架状态
        computePackage.setStatus(exist.getStatus());

        int rows = computePackageMapper.update(computePackage);

        if (rows == 0) {
            throw new RuntimeException("修改充值套餐失败");
        }
    }

    //上架套餐
    @Override
    @Transactional
    public void publish(Long id) {

        ComputePackage computePackage = getById(id);

        if (ComputePackageStatus.ON_SHELF.getCode().equals(computePackage.getStatus())) {
            throw new RuntimeException("充值套餐已经上架");
        }

        int rows = computePackageMapper.updateStatus(id, ComputePackageStatus.ON_SHELF.getCode());

        if (rows == 0) {
            throw new RuntimeException("充值套餐上架失败");
        }
    }

    //下架套餐
    @Override
    @Transactional
    public void unpublish(Long id) {

        ComputePackage computePackage = getById(id);

        if (ComputePackageStatus.OFF_SHELF.getCode().equals(computePackage.getStatus())) {
            throw new RuntimeException("充值套餐已经下架");
        }

        int rows = computePackageMapper.updateStatus(id, ComputePackageStatus.OFF_SHELF.getCode());

        if (rows == 0) {
            throw new RuntimeException("充值套餐下架失败");
        }
    }

    //校验套餐基础信息
    private void validatePackage(ComputePackage computePackage) {
        if (computePackage.getPackageName() == null || computePackage.getPackageName().trim().isEmpty()) {
            throw new RuntimeException("套餐名称不能为空");
        }

        if (computePackage.getPoints() == null || computePackage.getPoints() <= 0) {
            throw new RuntimeException("套餐算力必须大于0");
        }

        if (computePackage.getPrice() == null || computePackage.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("套餐价格必须大于0");
        }
    }
}
