package org.test.Ai_demo.services.summarizer.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.test.Ai_demo.config.properties.OpenAiProb;
import org.test.Ai_demo.enums.ProviderId;
import org.test.Ai_demo.exception.SummarizerException;
import org.test.Ai_demo.models.dto.SummarizationInput;
import org.test.Ai_demo.models.dto.SummarizationOutput;
import org.test.Ai_demo.services.summarizer.SummarizerPort;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientResponseException;

@Component
public class OpenAiSummarizer implements SummarizerPort {

    private final RestClient http;
    private final OpenAiProb props;

    public OpenAiSummarizer(RestClient.Builder builder, OpenAiProb props) {
        this.props = props;
        this.http = builder
                .baseUrl(props.baseUrl())
                .defaultHeader("Authorization", "Bearer " + props.apiKey())
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public ProviderId id() {
        return ProviderId.OPENAI;
    }

    @Override
    public SummarizationOutput summarize(SummarizationInput input) {
        var payload = new ChatRequest(
                props.model(),
                new ChatMessage[]{
                        new ChatMessage("system", "Summarize clearly, concisely, and factually. Output plain text. again output plain summarized text"),
                        new ChatMessage("user", input.text())
                },
                null, null, null
        );

        try {
            var resp = http.post()
                    .uri("/v1/chat/completions")
                    .body(payload)
                    .retrieve()
                    .onStatus(s -> s.value() == 401, (req, res) -> {
                        throw new SummarizerException("INVALID_API_KEY", "Auth failed", null);
                    })
                    .onStatus(s -> s.value() == 429, (req, res) -> {
                        throw new SummarizerException("RATE_LIMIT", "Rate limit hit", null);
                    })
                    .onStatus(s -> s.is5xxServerError(), (req, res) -> {
                        throw new SummarizerException("PROVIDER_ERROR", "Upstream error " + res.getStatusCode(), null);
                    })
                    .toEntity(ChatResponse.class)
                    .getBody();

            if (resp == null || resp.choices == null || resp.choices.length == 0)
                throw new SummarizerException("PROVIDER_ERROR", "Empty response", null);

            var content = resp.choices[0].message.content;
            return new SummarizationOutput(content, resp.model);

        } catch (RestClientResponseException ex) {
            if (ex.getStatusCode().is4xxClientError())
                throw new SummarizerException("INVALID_REQUEST", ex.getResponseBodyAsString(), ex);
            throw new SummarizerException("PROVIDER_ERROR", ex.getMessage(), ex);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record ChatRequest(String model, ChatMessage[] messages, Double temperature, Double top_p, Integer max_tokens) {
    }

    record ChatMessage(String role, String content) {
    }

    record ChatResponse(String model, Choice[] choices) {
    }

    record Choice(int index, ChatMessage message) {
    }
}

