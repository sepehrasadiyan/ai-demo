package org.test.Ai_demo.controller.publics;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.test.Ai_demo.enums.ProviderId;
import org.test.Ai_demo.models.dto.SummarizationOutput;
import org.test.Ai_demo.models.dto.SummarizationSettings;
import org.test.Ai_demo.models.request.SummarizeRequest;
import org.test.Ai_demo.models.response.SummarizeResponse;
import org.test.Ai_demo.services.SummarizationService;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/summaries", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class SummarizationController {

    private final SummarizationService service;

    public SummarizationController(SummarizationService service) {
        this.service = service;
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public SummarizeResponse summarize(
            @RequestHeader("X-User-Id") String userIdHeader,//I should read this from jwt
            @Valid @RequestBody SummarizeRequest req
    ) {
        final UUID userId;
        try {
            userId = UUID.fromString(userIdHeader);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid X-User-Id header: must be a UUID");
        }

        var settings = new SummarizationSettings(
                userId,
                req.dateTime() != null ? req.dateTime() : LocalDateTime.now()
        );

        SummarizationOutput out = service.execute(req.text(), settings, req.provider());
        return new SummarizeResponse(out.summary(), out.model());
    }
}
