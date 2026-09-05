package com.example.computingpowerrental.service;

import com.example.computingpowerrental.entity.AiTaskMessage;

/**
 * @author Lark
 * @ date 2026/8/7  16:47
 * @ description
 */
public interface AiTaskMessageProducer {
    void send(AiTaskMessage message);
}
