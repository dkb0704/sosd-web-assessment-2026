package com.fzujxl.aicomputerplatform.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//签到参数配置
@Data
@Component
@ConfigurationProperties(prefix = "sign")
public class SignConfig {

    private int basePoints = 5;

    private int streakBonus = 10;

    private int streakCycle = 7;
}
