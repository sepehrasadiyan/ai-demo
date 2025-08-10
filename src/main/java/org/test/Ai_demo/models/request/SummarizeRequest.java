package org.test.Ai_demo.models.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.test.Ai_demo.enums.ProviderId;

import java.time.LocalDateTime;

public record SummarizeRequest(
        @NotBlank @Size(min = 1, max = 200_000) String text,
        ProviderId provider,
        LocalDateTime dateTime
) {
}
