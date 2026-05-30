package com.aiplatform.service.ai;

import com.aiplatform.client.aimodel.MockAIServiceClient;
import com.aiplatform.client.aimodel.SpringAiClient;
import com.aiplatform.config.AiModelProperties;
import com.aiplatform.entity.AiModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIService {

    private final ChatClient.Builder chatClientBuilder;
    private final AiModelProperties aiModelProperties;
    private final SpringAiClient springAiClient;
    private final MockAIServiceClient mockAIServiceClient;

    /**
     * 文本生成：先尝试所有真实模型（Ollama），全部失败则降级到Mock
     */
    public String generateText(String prompt) {
        String wrappedPrompt = "请直接输出以下要求的最终结果，不要包含任何前缀、后缀、解释、语气词或礼貌用语。\n" + prompt;

        List<String> modelList = aiModelProperties.getTextModels();
        if (modelList == null || modelList.isEmpty()) {
            log.warn("未配置真实文本模型，直接使用Mock");
            return mockText(prompt);
        }

        for (String modelName : modelList) {
            log.info("尝试真实文本模型: {}", modelName);
            try {
                ChatClient dynamicClient = chatClientBuilder
                        .defaultOptions(ChatOptions.builder().model(modelName))  // 直接传 Builder
                        .build();
                String result = dynamicClient.prompt(wrappedPrompt).call().content();  // 使用包装后的 prompt
                log.info("真实模型 {} 调用成功", modelName);
                return result;
            } catch (Exception e) {
                log.warn("真实模型 {} 调用失败: {}", modelName, e.getMessage());
            }
        }

        log.warn("所有真实文本模型均失败，降级使用Mock数据");
        return mockText(prompt);
    }

    /**
     * 图像生成：先尝试真实图像API（Pollinations），失败则降级到Mock
     */
    public String generateImage(String prompt) {
        try {
            AiModel imageModel = new AiModel();
            imageModel.setModelKey("image");
            String imageUrl = springAiClient.generate(imageModel, prompt, null);
            log.info("真实图像生成成功: {}", imageUrl);
            return imageUrl;
        } catch (Exception e) {
            log.warn("真实图像生成失败: {}，降级使用Mock数据", e.getMessage());
            return mockImage(prompt);
        }
    }

    private String mockText(String prompt) {
        AiModel mockModel = new AiModel();
        mockModel.setModelKey("text");
        return mockAIServiceClient.generate(mockModel, prompt, null);
    }

    private String mockImage(String prompt) {
        AiModel mockModel = new AiModel();
        mockModel.setModelKey("image");
        return mockAIServiceClient.generate(mockModel, prompt, null);
    }
}