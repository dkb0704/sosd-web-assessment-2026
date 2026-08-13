package com.example.computingpowerrental.service.impl;

import com.example.computingpowerrental.entity.AiModel;
import com.example.computingpowerrental.enums.AiCategory;
import com.example.computingpowerrental.enums.AiModelStatus;
import com.example.computingpowerrental.enums.AiProvider;
import com.example.computingpowerrental.mapper.AiModelMapper;
import com.example.computingpowerrental.service.AiModelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Lark
 * @ date 2026/7/27  17:10
 * @ description AI模型业务实现类
 */
@Service
public class AiModelServiceImpl implements AiModelService {
    private final AiModelMapper aiModelMapper;


    public AiModelServiceImpl(AiModelMapper aiModelMapper) {
        this.aiModelMapper = aiModelMapper;
    }


    //根据ID查询模型
    @Override
    public AiModel getById(Long id) {

        AiModel model = aiModelMapper.findById(id);

        if (model == null) {
            throw new RuntimeException("模型不存在");
        }

        return model;
    }


    //查询全部模型（管理端使用）
    @Override
    public List<AiModel> listAll() {

        return aiModelMapper.findAll();

    }


    //查询已上架模型（用户端使用）
    @Override
    public List<AiModel> listPublished() {

        return aiModelMapper.findPublishedModels();

    }


    //新增模型
    @Override
    @Transactional
    public void addModel(AiModel aiModel) {


        //检查模型编码是否重复
        AiModel exist =
                aiModelMapper.findByModelCode(
                        aiModel.getModelCode()
                );


        if (exist != null) {

            throw new RuntimeException(
                    "模型编码已存在"
            );

        }


        //校验分类是否合法
        if (!AiCategory.isValid(aiModel.getCategory())) {

            throw new RuntimeException(
                    "不支持的模型分类"
            );

        }


        //校验Provider是否合法
        if (!AiProvider.isValid(aiModel.getProvider())) {

            throw new RuntimeException(
                    "不支持的模型提供商"
            );

        }


        //设置默认状态
        aiModel.setStatus(
                AiModelStatus.NOT_PUBLISHED.getCode()
        );


        //保存数据库
        int rows =
                aiModelMapper.insert(aiModel);


        if (rows == 0) {

            throw new RuntimeException(
                    "新增模型失败"
            );

        }

    }


    //修改模型基础信息
    @Override
    @Transactional
    public void updateModel(AiModel aiModel) {


        AiModel exist =
                getById(aiModel.getId());


        //不允许修改模型编码
        if (!exist.getModelCode()
                .equals(aiModel.getModelCode())) {

            throw new RuntimeException(
                    "不允许修改模型编码"
            );

        }


        int rows =
                aiModelMapper.update(aiModel);


        if (rows == 0) {

            throw new RuntimeException(
                    "修改模型失败"
            );

        }

    }


    //上架模型
    @Override
    @Transactional
    public void publish(Long id) {


        AiModel model = getById(id);


        //已经上架，无需重复操作
        if (AiModelStatus.PUBLISHED
                .getCode()
                .equals(model.getStatus())) {

            throw new RuntimeException(
                    "模型已经上架"
            );

        }


        int rows =
                aiModelMapper.updateStatus(
                        id,
                        AiModelStatus.PUBLISHED.getCode()
                );


        if (rows == 0) {

            throw new RuntimeException(
                    "模型上架失败"
            );

        }

    }


    //下架模型
    @Override
    @Transactional
    public void unpublish(Long id) {


        AiModel model = getById(id);


        if (AiModelStatus.UNPUBLISHED
                .getCode()
                .equals(model.getStatus())) {

            throw new RuntimeException(
                    "模型已经下架"
            );

        }


        int rows =
                aiModelMapper.updateStatus(
                        id,
                        AiModelStatus.UNPUBLISHED.getCode()
                );


        if (rows == 0) {

            throw new RuntimeException(
                    "模型下架失败"
            );

        }

    }


    //修改模型算力价格
    @Override
    @Transactional
    public void updateCostPoints(Long id,
                                 Integer costPoints) {


        if (costPoints == null
                || costPoints < 0) {

            throw new RuntimeException(
                    "算力价格不能小于0"
            );

        }


        getById(id);


        int rows =
                aiModelMapper.updateCostPoints(
                        id,
                        costPoints
                );


        if (rows == 0) {

            throw new RuntimeException(
                    "修改算力价格失败"
            );

        }

    }
}
