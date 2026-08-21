package com.fzujxl.aicomputerplatform.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

public class SnowFlake {
    // 静态常量：类加载时只初始化一次，保证单例
    // 参数 (workerId, datacenterId) 单机直接写死 1,1 即可
    private static final Snowflake SNOWFLAKE = IdUtil.getSnowflake(1, 1);

    /**
     * 生成雪花ID（Long类型）
     */
    public static Long nextId() {
        return SNOWFLAKE.nextId();
    }

    /**
     * 生成雪花ID（String类型，防止前端JS丢失精度）
     */
    public static String nextIdStr() {
        return SNOWFLAKE.nextIdStr();
    }
}
