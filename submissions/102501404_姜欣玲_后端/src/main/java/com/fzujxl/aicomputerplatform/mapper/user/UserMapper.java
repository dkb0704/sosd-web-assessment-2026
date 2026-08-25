package com.fzujxl.aicomputerplatform.mapper.user;

import com.fzujxl.aicomputerplatform.entity.User;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@Mapper
public interface UserMapper {

    //注册添加用户
    @Insert("insert into `user` (password, create_time, update_time, point, sign_days, email, user_name, status)" +
            " values (#{password},#{createTime},#{updateTime},#{point},#{signDays},#{email},#{userName},#{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    //根据用户名或者邮箱查询正常状态的用户
    User selectByAccount(@Param("account") String account);

    //根据用户名查询用户
    @Select("select user.user_name from `user` where user_name=#{userName} limit 1")
    User selectByUserName(@Param("userName") String userName);

    //根据用户名或者邮箱查询用户token_version
    User selectVersion(@Param("account") String account);

    //根据ID查询用户
    @Select("SELECT id, user_name, email, point, role, status, create_time, update_time, last_sign_date, sign_days FROM user WHERE id = #{id} AND deleted = 0")
    User selectById(@Param("id") Long id);

    //根据用户名查询用户（查重，排除指定ID）
    @Select("SELECT id FROM user WHERE deleted = 0 AND user_name = #{userName} AND id != #{userId} LIMIT 1")
    User selectByUserNameExcludeUserId(@Param("userName") String userName, @Param("userId") Long userId);

    //根据邮箱查询用户（查重，排除指定ID）
    @Select("SELECT id FROM user WHERE deleted = 0 AND email = #{email} AND id != #{userId} LIMIT 1")
    User selectByEmailExcludeUserId(@Param("email") String email, @Param("userId") Long userId);

    //更新用户信息（动态更新，SQL定义在 UserMapper.xml）
    int updateUser(User user);

    //更新签到信息
    @Update("UPDATE user SET point = #{point}, last_sign_date = #{lastSignDate}, sign_days = #{signDays}, update_time = NOW() WHERE id = #{id}")
    int updateSignInfo(@Param("id") Long id, @Param("point") BigDecimal point, @Param("lastSignDate") LocalDate lastSignDate, @Param("signDays") Integer signDays);
}