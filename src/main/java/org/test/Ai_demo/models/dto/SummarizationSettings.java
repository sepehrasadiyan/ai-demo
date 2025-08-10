package org.test.Ai_demo.models.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record SummarizationSettings(UUID userId, LocalDateTime dateTime) {
}
