package com.example.computingpowerrental.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author Lark
 * @ date 2026/5/27  17:01
 * @ description 修改个人资料请求DTO（算力点数等字段不允许修改，只能修改昵称、头像、手机号等）
 */
@Data
public class UpdateProfileRequest {
    //昵称（有限制长度）
    @Size(max = 30, message = "昵称长度不能超过30个字符")
    private String nickname;

    //头像地址
    @Size(max = 255, message = "头像地址长度不能超过255个字符")
    private String avatar;

    //手机号，用正则限制格式要求（1开头，后面还有10位）
    @Pattern(regexp = "^1\\d{10}$", message = "手机号格式不正确")
    private String phone;
}
