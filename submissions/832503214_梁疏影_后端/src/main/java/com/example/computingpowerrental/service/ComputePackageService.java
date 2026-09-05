package com.example.computingpowerrental.service;

import com.example.computingpowerrental.entity.ComputePackage;

import java.util.List;

/**
 * @author Lark
 * @ date 2026/8/15  16:35
 * @ description 算力充值套餐业务接口
 */
public interface ComputePackageService {
    //根据ID查询套餐
    ComputePackage getById(Long id);

    //查询全部套餐（管理端）
    List<ComputePackage> listAll();

    //查询已上架套餐（用户端）
    List<ComputePackage> listOnShelf();

    //新增套餐
    void addPackage(ComputePackage computePackage);

    //修改套餐
    void updatePackage(ComputePackage computePackage);

    //上架套餐
    void publish(Long id);

    //下架套餐
    void unpublish(Long id);
}
