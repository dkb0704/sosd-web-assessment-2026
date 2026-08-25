package com.example.computingpowerrental.mapper;

import com.example.computingpowerrental.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Lark
 * @ date 2026/5/24  17:24
 * @ description 用户mapper
 */
@Mapper
public interface UserMapper {
    User findById(@Param("id") Long id);
    User findByUsername(@Param("username") String username);
    User findByEmail(@Param("email") String email);
    User findByAccount(@Param("account") String account);
    int insert(User user);
    int update(User user);
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    int updateComputePoints(@Param("id") Long id,
                            @Param("computePoints") Integer computePoints);
    //原子增加用户算力点数
    int increaseComputePoints(@Param("id") Long id,
                              @Param("points") Integer points);
}
