package com.token.tokenpocket.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.token.tokenpocket.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
