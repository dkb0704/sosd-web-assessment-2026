package com.aiplatform.client.aimodel;

import com.aiplatform.config.AiModelProperties;
import com.aiplatform.entity.AiModel;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@Profile("ai")
@Primary
@RequiredArgsConstructor
public class SpringAiClient implements AIServiceClient {

    private final AiModelProperties aiModelProperties;

    @PostConstruct
    public void init() {
        log.info("SpringAiClient 初始化（真实图像生成）");
    }

    @Override
    public String generate(AiModel model, String promptText, String fileUrls) {
        // 只处理图像生成
        if (!"image".equals(model.getModelKey())) {
            throw new IllegalArgumentException("SpringAiClient 只支持图像生成，不支持的模型类型: " + model.getModelKey());
        }

        try {
            log.info("调用真实图像生成 API，prompt：{}", promptText);
            String encodedPrompt = UriUtils.encodePathSegment(promptText, StandardCharsets.UTF_8);
            String baseUrl = aiModelProperties.getImagePollinations().getBaseUrl();
            if (!baseUrl.endsWith("/")) {
                baseUrl += "/";
            }
            String imageUrl = baseUrl + encodedPrompt;
            log.info("真实图像生成 URL：{}", imageUrl);
            return imageUrl;
        } catch (Exception e) {
            log.error("真实图像生成失败", e);
            throw new RuntimeException("图像生成失败: " + e.getMessage(), e);
        }
    }
}