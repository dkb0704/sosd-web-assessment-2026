package com.aiplatform.controller.ai;

import com.aiplatform.service.ai.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AIController {

    private final AIService aiService;

    @PostMapping("/text")
    public String text(@RequestParam String prompt) {
        return aiService.generateText(prompt);
    }

    @PostMapping("/image")
    public String image(@RequestParam String prompt) {
        return aiService.generateImage(prompt);
    }
}