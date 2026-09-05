package com.example.computingpowerrental.dto;

import lombok.Data;

/**
 * @author Lark
 * @ date 2026/8/7  16:28
 * @ description 查询DTO
 */
@Data
public class TaskQueryRequest {
    private Integer page = 1;   //页码
    private Integer size = 10;   //每页数量
    private Integer status;   //任务状态，可为空
}
