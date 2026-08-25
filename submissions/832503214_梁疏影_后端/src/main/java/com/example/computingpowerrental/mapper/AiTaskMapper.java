package com.example.computingpowerrental.mapper;

import com.example.computingpowerrental.entity.AiTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Lark
 * @ date 2026/7/16  10:47
 * @ description AI任务mapper
 */
@Mapper
public interface AiTaskMapper {
    //新增 AI 生成任务
    int insert(AiTask aiTask);

    //根据任务 ID 查询任务详情
    AiTask findById(@Param("id") Long id);

    //根据任务 ID 和用户 ID 查询任务
    AiTask findByIdAndUserId(@Param("id") Long id,
                             @Param("userId") Long userId);

    //查询指定用户的 AI 任务列表
    List<AiTask> findByUserId(@Param("userId") Long userId);

    //更新任务状态
    int updateStatus(@Param("id") Long id,
                     @Param("status") Integer status);

    //将任务更新为生成成功
    int updateSuccess(@Param("id") Long id,
                      @Param("status") Integer status,
                      @Param("result") String result);

    //将任务更新为生成失败
    int updateFailed(@Param("id") Long id,
                     @Param("status") Integer status,
                     @Param("errorMessage") String errorMessage);

    //更新任务公开状态
    int updatePublicStatus(@Param("id") Long id,
                           @Param("userId") Long userId,
                           @Param("isPublic") Boolean isPublic);
}
