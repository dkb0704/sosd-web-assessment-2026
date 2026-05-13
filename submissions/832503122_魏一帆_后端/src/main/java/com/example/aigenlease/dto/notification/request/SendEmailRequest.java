package com.example.aigenlease.dto.notification.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SendEmailRequest(

        @Email(message = "邮箱格式错误")
        @NotBlank(message = "邮箱不能为空")
        String to,

        @NotBlank(message = "邮件主题不能为空")
        String subject,

        @NotBlank(message = "邮件内容不能为空")
        String content

) {
}