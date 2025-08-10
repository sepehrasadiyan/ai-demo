package org.test.Ai_demo.services.history;

import org.springframework.stereotype.Service;
import org.test.Ai_demo.models.document.HistoryDocument;
import org.test.Ai_demo.repository.HistoryRepository;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Service
public class HistoryService {

    private final HistoryRepository repo;
    private final Clock clock;

    public HistoryService(HistoryRepository repo, Clock clock) {
        this.repo = repo;
        this.clock = clock;
    }

    public void recordSuccess(UUID userId, String provider, String model, String inputPreview, String summary) {
        HistoryDocument h = new HistoryDocument();
        h.setUserId(userId);
        h.setCreatedAt(Instant.now(clock));
        h.setProvider(provider);
        h.setModel(model);
        h.setInputPreview(inputPreview);
        h.setSummary(summary);
        repo.save(h);
    }

    public void recordFailure(UUID userId, String provider, String inputPreview, String code, String message) {
        HistoryDocument h = new HistoryDocument();
        h.setUserId(userId);
        h.setCreatedAt(Instant.now(clock));
        h.setProvider(provider);
        h.setInputPreview(inputPreview);
        h.setErrorCode(code);
        h.setErrorMessage(message);
        repo.save(h);
    }

    public static String preview(String s, int max) {
        if (s == null) return null;
        return s.length() <= max ? s : s.substring(0, max) + "…";
    }
}
