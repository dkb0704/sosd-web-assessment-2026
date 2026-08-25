package com.aiplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;

/**
 * 用户签到记录表
 */
@Data
@TableName("user_sign")
public class UserSign {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户 ID */
    private Long userId;

    /** 签到日期 */
    private LocalDate signDate;

    /** 连续签到天数 */
    private Integer continuousDays;
}