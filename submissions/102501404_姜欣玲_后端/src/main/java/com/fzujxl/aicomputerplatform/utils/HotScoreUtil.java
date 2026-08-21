package com.fzujxl.aicomputerplatform.utils;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

@Component
public class HotScoreUtil {

    // 默认参数（可调整）
    private static final double DEFAULT_BASE = 2.0;        // 分母基数
    private static final double DEFAULT_EXPONENT = 1.5;    // 衰减指数
    private static final int DEFAULT_SCALE = 10000;        // 缩放系数（将小数转为整数）

    private HotScoreUtil() {}

    /**
     * 计算热度（仅浏览量+时间），返回整数
     * @param viewCount  浏览量（long 防止溢出）
     * @param createTime 创建时间（Instant，UTC时区无关）
     * @return 整数热度（例如 87321）
     */
    public static long calculate(long viewCount, LocalDateTime createTime) {
        Instant createdAt = createTime.atZone(ZoneId.of("Asia/Shanghai")).toInstant();
        return calculate(viewCount, createdAt, DEFAULT_BASE, DEFAULT_EXPONENT, DEFAULT_SCALE);
    }

    /**
     * 自定义参数版本
     */
    public static long calculate(long viewCount, Instant createTime,
                                 double base, double exponent, int scale) {
        Objects.requireNonNull(createTime, "createTime must not be null");



        // 1. 计算发布至今的小时数（带小数，平滑衰减）
        double hours = Duration.between(createTime, Instant.now()).toMillis() / 3600000.0;
        if (hours < 0) hours = 0;

        // 2. 浏览量取自然对数，防止头部垄断（Math.log1p 安全处理零值）
        double logView = Math.log1p(Math.max(viewCount, 0));

        // 3. 原始浮点热度
        double rawScore = logView / Math.pow(hours + base, exponent);

        // 4. 缩放并四舍五入为整数
        return Math.round(rawScore * scale);
    }
}