package com.example.computingpowerrental.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Lark
 * @ date 2026/8/7  16:45
 * @ description 消息实体
 */
@Data
public class AiTaskMessage implements Serializable{
    private static final long serialVersionUID = 1L;
    private Long taskId;
    public AiTaskMessage(Long taskId){
        this.taskId = taskId;
    }
}
