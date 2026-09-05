package com.example.computingpowerrental.dto;

/**
 * @author Lark
 * @ date 2026/9/2  22:44
 * @ description 管理员修改用户资料请求
 */
public class AdminUpdateUserRequest {
    private String nickname;
    private String email;
    private String phone;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
