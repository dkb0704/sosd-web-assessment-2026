package com.example.computingpowerrental.dto;

import com.example.computingpowerrental.entity.User;
import lombok.Data;

/**
 * @author Lark
 * @ date 2026/5/24  16:54
 * @ description 登录相应DTO
 */
@Data
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private Long expiresIn;   //access剩余有效期，单位为秒
    private UserInfo userInfo;

    @Data
    public static class UserInfo {
        private Long id;
        private String username;
        private String email;
        private String nickname;
        private String avatar;
        private String phone;
        private Integer role;   //0用户，1管理员
        private Integer status;   //0禁用，1正常
        private Integer computePoints;   //算力点数余额

        public static UserInfo fromUser(User user) {
            if (user == null) return null;
            UserInfo info = new UserInfo();
            info.setId(user.getId());
            info.setUsername(user.getUsername());
            info.setEmail(user.getEmail());
            info.setNickname(user.getNickname());
            info.setAvatar(user.getAvatar());
            info.setPhone(user.getPhone());
            info.setRole(user.getRole());
            info.setStatus(user.getStatus());
            info.setComputePoints(user.getComputePoints());
            return info;
        }
    }
}
