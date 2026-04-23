package com.token.tokenpocket.config;

import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterception implements HandlerInterceptor {
    //1.获取token
    String tok
    //2.基于token来获取redis中的用户
    //3.判断用户是不是存在
    //4.不存在，拦截，返回错误信息
    //5.将查询到的hash变成UserDTO
    //6.刷新token有效期
}
