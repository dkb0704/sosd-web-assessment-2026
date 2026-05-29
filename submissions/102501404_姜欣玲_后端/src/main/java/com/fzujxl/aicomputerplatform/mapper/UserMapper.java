package com.fzujxl.aicomputerplatform.mapper;

import com.fzujxl.aicomputerplatform.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    //注册添加用户
    @Insert("insert into `user` (password, create_time, update_time, point, sign_days, email, userName, status)" +
            " values (#{password},#{createTime},#{updateTime},#{point},#{signDays},#{email},#{userName},#{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    //根据用户名或者邮箱查询正常状态的用户
    @Select("select password, create_time, update_time, point, sign_days, id, email, userName, status, deleted" +
            " from `user` where (userName=#{account} or email=#{account}) and deleted = 0")
    User selectByAccount(@Param("account") String account);

    //查询用户信息
    @Select("select `user`. create_time, update_time, point, sign_days, id, email, userName ,status from `user`")
    User select();

    //根据用户名查询用户
    @Select("select user.password, user.create_time, user.update_time, user.point, user.sign_days," +
            " user.id, user.email, user.userName, user.status from user where userName=#{userName}")
    User selectBYUserName(String userName);

    //根据邮箱查询用户
    @Select("select user.password, user.create_time, user.update_time, user.point, user.sign_days," +
            " user.id, user.email, user.userName, user.status from user where email=#{email}")
    User selectBYEmail(String email);

    //查询所有用户
    @Select("select * from user")
    List<User> selectAll();
}
