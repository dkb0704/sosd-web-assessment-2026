package com.example.computingpowerrental.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * @author Lark
 * @ date 2026/5/24  16:37
 * @ description 用户实体类
 */
@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String nickname;
    private String avatar;
    private String phone;
    private Integer role;   //0：用户 1：管理员
    private Integer status;   //0：禁用 1：正常
    private Integer computePoints;   //当前算力点数余额
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='[PROTECTED]'" +
                ", email='" + email + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", phone='" + phone + '\'' +
                ", role=" + role +
                ", status=" + status +
                ", computePoints=" + computePoints +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
