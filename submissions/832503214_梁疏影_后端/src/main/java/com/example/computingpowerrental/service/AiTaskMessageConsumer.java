package com.example.computingpowerrental.service;

import com.example.computingpowerrental.entity.AiTaskMessage;

/**
 * @author Lark
 * @ date 2026/8/7  16:56
 * @ description
 */
public interface AiTaskMessageConsumer {
    void consume(
            AiTaskMessage message
    );
}
