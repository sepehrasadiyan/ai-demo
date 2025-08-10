package org.test.Ai_demo.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ai.openai")
public class OpenAiProb {
    private String baseUrl;
    private String apiKey;
    private String model;
    public String baseUrl() { return baseUrl; }
    public String apiKey() { return apiKey; }
    public String model() { return model; }
    public void setBaseUrl(String v) { baseUrl = v; }
    public void setApiKey(String v) { apiKey = v; }
    public void setModel(String v) { model = v; }
}
