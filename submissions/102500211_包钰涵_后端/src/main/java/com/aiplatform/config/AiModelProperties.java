package com.aiplatform.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.util.ArrayList;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "ai")
public class AiModelProperties {
    private List<String> textModels = new ArrayList<>();
    private ImagePollinations imagePollinations = new ImagePollinations();

    @Data
    public static class ImagePollinations {
        private String baseUrl = "https://image.pollinations.ai/prompt/";
    }
}