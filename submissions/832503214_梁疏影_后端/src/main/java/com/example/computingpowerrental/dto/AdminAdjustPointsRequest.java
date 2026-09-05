package com.example.computingpowerrental.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * @author Lark
 * @ date 2026/9/2  22:45
 * @ description 管理员调整用户算力请求
 */
public class AdminAdjustPointsRequest {
    @NotNull(message = "算力点数不能为空")
    @Positive(message = "算力点数必须大于0")
    private Integer points;

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
