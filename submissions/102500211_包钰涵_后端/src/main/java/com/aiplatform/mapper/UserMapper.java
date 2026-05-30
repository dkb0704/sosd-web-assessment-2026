package com.aiplatform.mapper;

import com.aiplatform.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 用户 Mapper
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 原子更新算力点数（防止超扣）
     * SQL：UPDATE user SET points = points + #{points} WHERE id = #{userId} AND points + #{points} >= 0
     *
     * @param userId 用户 ID
     * @param points 变化的点数（正数增加，负数扣减）
     * @return 影响行数（0 表示算力不足或用户不存在）
     */
    @Update("UPDATE user SET points = points + #{points} WHERE id = #{userId} AND points + #{points} >= 0")
    int updatePoints(@Param("userId") Long userId, @Param("points") int points);
}