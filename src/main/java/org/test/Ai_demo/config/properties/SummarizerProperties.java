package org.test.Ai_demo.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.test.Ai_demo.enums.ProviderId;

@Configuration
@ConfigurationProperties(prefix = "summarizer")
public class SummarizerProperties {

    private ProviderId defaultProvider = ProviderId.OPENAI;

    public ProviderId defaultProvider() {
        return defaultProvider;
    }

    public void setDefaultProvider(ProviderId p) {
        this.defaultProvider = p;
    }
}