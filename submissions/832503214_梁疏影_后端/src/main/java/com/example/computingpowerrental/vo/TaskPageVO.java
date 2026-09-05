package com.example.computingpowerrental.vo;

import com.example.computingpowerrental.entity.AiTask;
import lombok.Data;

import java.util.List;

/**
 * @author Lark
 * @ date 2026/8/7  16:29
 * @ description
 */
@Data
public class TaskPageVO {
    private List<AiTask> records;
    private Integer page;
    private Integer size;
    private Long total;
}
