package com.aiplatform.client.aimodel;

import com.aiplatform.entity.AiModel;

public interface AIServiceClient {
    String generate(AiModel model, String prompt, String fileUrls);
}