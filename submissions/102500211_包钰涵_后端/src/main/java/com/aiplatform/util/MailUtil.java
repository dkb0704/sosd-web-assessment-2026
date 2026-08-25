package com.aiplatform.util;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailUtil {

    private final JavaMailSender mailSender;

    @Async
    public void sendSimpleMail(String to, String subject, String content) {
        try {
            log.info("准备发送邮件：to={}, subject={}", to, subject);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);

            // 先不手动设置 from，让 Spring Boot 自动使用 yml 中的 username
            // 如果想显式设置，确保和 yml 中的 username 完全一致
            // message.setFrom("你的QQ邮箱@qq.com");

            mailSender.send(message);
            log.info("邮件发送成功：to={}", to);
        } catch (Exception e) {
            log.error("邮件发送失败：to={}, 错误类型={}, 错误信息={}",
                    to, e.getClass().getSimpleName(), e.getMessage());
            // 打印完整堆栈，方便定位
            log.error("完整错误堆栈：", e);
        }
    }
}