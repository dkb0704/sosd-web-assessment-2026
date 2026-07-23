package com.example.computingpowerrental.dto;

import com.example.computingpowerrental.entity.User;
import lombok.Data;

/**
 * @author Lark
 * @ date 2026/5/26  15:04
 * @ description 用户信息响应DTO（返回当前登录用户的信息）
 */
@Data
public class UserInfoResponse {
    private Long id;
    private String username;
    private String email;
    private String nickname;
    private String avatar;
    private String phone;
    private Integer role;   //0普通用户，1管理员
    private Integer status;   //用户状态：0禁用，1正常
    private Integer computePoints;   //当前算力点数余额

    //将User实体转换为UserInfoResponse
    public static UserInfoResponse fromUser(User user) {
        if (user == null) {
            return null;
        }

        UserInfoResponse response = new UserInfoResponse();

        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setNickname(user.getNickname());
        response.setAvatar(user.getAvatar());
        response.setPhone(user.getPhone());
        response.setRole(user.getRole());
        response.setStatus(user.getStatus());
        response.setComputePoints(user.getComputePoints());

        return response;
    }
}
