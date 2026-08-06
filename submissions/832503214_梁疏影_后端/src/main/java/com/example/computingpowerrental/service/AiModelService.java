package com.example.computingpowerrental.service;

import com.example.computingpowerrental.entity.AiModel;

import java.util.List;

/**
 * @author Lark
 * @ date 2026/7/27  17:08
 * @ description 负责AI模型相关业务逻辑
 */
public interface AiModelService {
    //根据 ID 查询模型
    AiModel getById(Long id);


    //查询所有模型
    List<AiModel> listAll();


    //查询所有已上架模型
    List<AiModel> listPublished();


    //新增模型
    void addModel(AiModel aiModel);


    //修改模型基础信息
    void updateModel(AiModel aiModel);


    //上架模型
    void publish(Long id);


    //下架模型
    void unpublish(Long id);


    //修改模型算力价格
    void updateCostPoints(Long id,
                          Integer costPoints);
}
