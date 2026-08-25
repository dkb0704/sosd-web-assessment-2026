package com.aiplatform.client.aimodel;

import com.aiplatform.entity.AiModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MockAIServiceClient implements AIServiceClient {
    @Override
    public String generate(AiModel model, String prompt, String fileUrls) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        String modelKey = model.getModelKey();
        if ("text".equals(modelKey)) {
            return "这是AI根据提示词「" + prompt + "」生成的文本回复。";
        } else if ("image".equals(modelKey)) {
            return "https://picsum.photos/800/600?random=" + System.currentTimeMillis();
        } else {
            return "未知模型类型";
        }
    }
}