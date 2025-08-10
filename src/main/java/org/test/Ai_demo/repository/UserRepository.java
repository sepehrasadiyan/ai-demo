package org.test.Ai_demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.test.Ai_demo.models.document.UserDocument;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends MongoRepository<UserDocument, UUID> {

    Optional<UserDocument> findById(UUID id);

    Optional<UserDocument> findByIdAndActiveTrue(UUID id);

    long countByActiveTrue();

    long countByPlan(String plan);
}
