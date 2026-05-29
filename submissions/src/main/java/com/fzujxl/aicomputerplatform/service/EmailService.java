package com.fzujxl.aicomputerplatform.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendWelcomeEmail(String toEmail, String username) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("欢迎注册AI创作与算力租赁平台");
            message.setText("尊敬的 " + username + "：\n\n" +
                    "欢迎您注册AI创作与算力租赁平台！\n\n" +
                    "您的账户已成功创建，初始积分为50分。\n" +
                    "您现在可以开始使用我们的AI创作服务和算力租赁服务。\n\n" +
                    "如有任何问题，请随时联系我们的客服团队。\n\n" +
                    "祝您使用愉快！\n" +
                    "AI创作与算力租赁平台团队");

            mailSender.send(message);
            log.info("欢迎邮件发送成功: {}", toEmail);
        } catch (Exception e) {
            log.error("发送欢迎邮件失败: {}, 错误: {}", toEmail, e.getMessage());
        }
    }
}
