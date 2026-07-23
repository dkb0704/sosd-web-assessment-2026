package com.example.computingpowerrental.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Lark
 * @ date 2026/7/15  17:57
 * @ description 用户签到响应DTO，返回签到获得的算力点数和签到后的最新算力余额
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignInResponse {
    //本次签到获得的算力点数
    private Integer rewardPoints;

    //签到后的最新算力点余额
    private Integer currentPoints;
}
