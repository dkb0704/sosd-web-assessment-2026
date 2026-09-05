package com.example.computingpowerrental.service;

import com.example.computingpowerrental.dto.SignInResponse;
import com.example.computingpowerrental.dto.UpdatePasswordRequest;
import com.example.computingpowerrental.dto.UpdateProfileRequest;
import com.example.computingpowerrental.dto.UserInfoResponse;
import com.example.computingpowerrental.dto.AdminUpdateUserRequest;
import com.example.computingpowerrental.vo.AdminUserPageVO;

/**
 * @author Lark
 * @ date 2026/5/27  17:13
 * @ description 用户模块业务接口
 */
public interface UserService {
    //获取当前登录用户信息
    UserInfoResponse getCurrentUserInfo();

    //修改当前登录用户的个人资料
    UserInfoResponse updateProfile(UpdateProfileRequest request);

    //修改当前登录用户密码
    void updatePassword(UpdatePasswordRequest request);

    //获取当前登录用户的算力点余额
    Integer getCurrentUserPoints();

    //前用户每日签到领取算力点
    SignInResponse signIn();

    //管理端分页查询用户
    AdminUserPageVO adminPageUsers(Integer page, Integer size, Integer status);

    //管理端查询用户详情
    UserInfoResponse adminGetUser(Long userId);

    //管理端修改用户资料
    UserInfoResponse adminUpdateUser(Long userId, AdminUpdateUserRequest request);

    //管理端启用/禁用用户
    void adminUpdateStatus(Long userId, Integer status);

    //管理端增加用户算力
    void adminAddPoints(Long userId, Integer points);

    //管理端扣除用户算力
    void adminDeductPoints(Long userId, Integer points);
}
