package com.fzujxl.aicomputerplatform.config;

import com.fzujxl.aicomputerplatform.utils.BCryptPasswordEncoder;
import com.fzujxl.aicomputerplatform.utils.PasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//密码编码器注册
@Configuration
public class SecurityBeanConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
