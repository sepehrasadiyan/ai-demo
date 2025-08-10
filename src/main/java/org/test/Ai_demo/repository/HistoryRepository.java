package org.test.Ai_demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.test.Ai_demo.models.document.HistoryDocument;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface HistoryRepository extends MongoRepository<HistoryDocument, String> {

    Page<HistoryDocument> findByUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);

    Page<HistoryDocument> findByUserIdAndProviderOrderByCreatedAtDesc(UUID userId, String provider, Pageable pageable);

    List<HistoryDocument> findTop20ByUserIdOrderByCreatedAtDesc(UUID userId);

    List<HistoryDocument> findTop20ByUserIdAndProviderOrderByCreatedAtDesc(UUID userId, String provider);

    Page<HistoryDocument> findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(UUID userId, Instant from, Instant to, Pageable pageable);

    long countByUserId(UUID userId);

    long countByUserIdAndProvider(UUID userId, String provider);
}
