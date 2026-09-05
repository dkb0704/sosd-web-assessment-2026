package com.example.computingpowerrental.mapper;

import com.example.computingpowerrental.entity.ComputePackage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Lark
 * @ date 2026/8/15  12:16
 * @ description
 */
@Mapper
public interface ComputePackageMapper {
    //根据ID查询套餐
    ComputePackage findById(@Param("id") Long id);

    //查询全部套餐
    List<ComputePackage> findAll();

    //查询已上架套餐
    List<ComputePackage> findByStatus(@Param("status") Integer status);

    //新增套餐
    int insert(ComputePackage computePackage);

    //更新套餐
    int update(ComputePackage computePackage);

    //修改套餐状态
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
}
