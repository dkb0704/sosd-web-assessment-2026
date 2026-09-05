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

    List<AiTask> findByUserId(@Param("userId") Long userId);

    //分页查询用户任务
    List<AiTask> findPageByUserId(
            @Param("userId") Long userId,
            @Param("status") Integer status,
            @Param("offset") Integer offset,
            @Param("size") Integer size
    );

    //查询任务总数
    Long countByUserId(
            @Param("userId") Long userId,
            @Param("status") Integer status
    );

    //查询公开作品列表
    List<AiTask> findPublicTasks(
            @Param("category") String category,
            @Param("sort") String sort,
            @Param("offset") Integer offset,
            @Param("size") Integer size
    );

    //查询公开作品数量
    Long countPublicTasks(
            @Param("category") String category
    );

    //更新任务状态
    int updateStatus(@Param("id") Long id,
                     @Param("oldStatus") Integer oldStatus,
                     @Param("newStatus") Integer newStatus);

    //将任务更新为生成成功
    int updateSuccess(@Param("id") Long id,
                      @Param("oldStatus") Integer oldStatus,
                      @Param("newStatus") Integer newStatus,
                      @Param("result") String result);

    //将任务更新为生成失败
    int updateFailed(@Param("id") Long id,
                     @Param("oldStatus") Integer oldStatus,
                     @Param("newStatus") Integer newStatus,
                     @Param("errorMessage") String errorMessage);

    //更新任务公开状态
    int updatePublicStatus(@Param("id") Long id,
                           @Param("userId") Long userId,
                           @Param("isPublic") Boolean isPublic);

    //公开作品点赞
    int increaseLikeCount(@Param("id") Long id);

    //管理端分页查询全站AI任务
    List<AiTask> findPage(
            @Param("status") Integer status,
            @Param("offset") Integer offset,
            @Param("size") Integer size
    );

    //管理端查询全站AI任务总数
    Long countAll(
            @Param("status") Integer status
    );
}
