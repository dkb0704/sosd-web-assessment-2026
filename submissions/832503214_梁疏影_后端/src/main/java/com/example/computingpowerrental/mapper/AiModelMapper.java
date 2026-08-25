package com.example.computingpowerrental.mapper;

import com.example.computingpowerrental.entity.AiModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Lark
 * @ date 2026/7/16  11:51
 * @ description AI模型mapper
 */
@Mapper
public interface AiModelMapper {
    //根据模型 ID 查询模型
    AiModel findById(@Param("id") Long id);

    //根据模型唯一编码查询模型
    AiModel findByModelCode(@Param("modelCode") String modelCode);

    //查询所有已上架模型
    List<AiModel> findPublishedModels();

    //根据分类查询已上架模型
    List<AiModel> findPublishedModelsByCategory(
            @Param("category") String category
    );

    //查询全部模型
    List<AiModel> findAll();

    //新增 AI 模型
    int insert(AiModel aiModel);

    //修改模型基础信息
    int update(AiModel aiModel);

    //修改模型状态
    int updateStatus(@Param("id") Long id,
                     @Param("status") Integer status);

    //调整模型每次调用的算力消耗单价
    int updateCostPoints(@Param("id") Long id,
                         @Param("costPoints") Integer costPoints);
}
