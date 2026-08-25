package com.example.computingpowerrental.service;

import com.example.computingpowerrental.dto.SignInResponse;
import com.example.computingpowerrental.dto.UpdatePasswordRequest;
import com.example.computingpowerrental.dto.UpdateProfileRequest;
import com.example.computingpowerrental.dto.UserInfoResponse;

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
}
