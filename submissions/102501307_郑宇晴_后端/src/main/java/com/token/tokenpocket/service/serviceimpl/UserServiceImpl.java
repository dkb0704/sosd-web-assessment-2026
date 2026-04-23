package com.token.tokenpocket.service.serviceimpl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.hutool.core.util.RandomUtil;
import com.token.tokenpocket.dto.LoginFormDTO;
import com.token.tokenpocket.dto.UserDTO;
import com.token.tokenpocket.mapper.UserMapper;
import com.token.tokenpocket.pojo.Result;
import com.token.tokenpocket.pojo.User;
import com.token.tokenpocket.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.token.tokenpocket.utils.RedisContants.*;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private StringRedisTemplate template;

    @Mapper
    private UserMapper userMapper;

    @Override
    public Result sendCode(String phone, HttpSession session) {
//1.校验手机号
        //TODO 正则表达式
        // if (Regexutils.isPhoneInvalid(phone)){}
        if (phone.length() != 11) {
//2.如果不符合，返回错误信息
            return Result.error("手机号格式错误");
        }
//3.符合，生成验证码
        String code = RandomUtil.randomNumbers(6);
//4.保存验证码到redis
        template.opsForValue().set(LOGIN_CODE_KEY, phone, LOGIN_CODE_TTL, TimeUnit.MINUTES);
//5.发送验证码
        log.debug("发送短信验证码成功，验证码：{}", code);
//返回0k
        return Result.success();
    }

    @Override
    public Result login(LoginFormDTO loginForm, HttpSession session) {
//1.校验手机号
        String phone = loginForm.getPhone();

        //todo Regexutils.isPhoneInvalid(phone)
        if (phone.length() != 11) {
//2,如果不符合，返回错误信息
            return Result.error("手机号格式错误！");
        }
//T0D03,从redis获取验证码并校验
        Object cachecode = template.opsForValue().get(LOGIN_CODE_KEY+phone);
        String code = loginForm.getCode();
        if (cachecode == null || cachecode.toString().equals(code)) {
//不一致，报错
            return Result.error("验证码错误");
        }
//4.一致，根据手机号查询用户select*from tb_.user where phone=?
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, phone);
        User user = userMapper.selectOne(queryWrapper);
//5.判断用户是否存在
        if (user == null) {
            //TODO
            user = createNewUserWithPhone(user);
        }
        String token  = UUID.randomUUID().toString();
        UserDTO userDTO = BeanUtil.copyProperties(user,UserDTO.class);
        BeanUtils.copyProperties(user,userDTO);
        Map<String,Object> userMap = BeanUtil.beanToMap(userDTO);
        template.opsForHash().putAll(LOGIN_USER_KEY+token,userMap);
        template.expire(LOGIN_USER_KEY+token,30,TimeUnit.MINUTES);

        return Result.success(token);
    }

    private User createNewUserWithPhone(User user) {
        //todo
        return new User();
    }
}
