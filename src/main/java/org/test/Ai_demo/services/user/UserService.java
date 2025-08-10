package org.test.Ai_demo.services.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.test.Ai_demo.exception.UserAccessException;
import org.test.Ai_demo.models.document.UserDocument;
import org.test.Ai_demo.repository.UserRepository;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;


@Service
public class UserService {
    private final UserRepository users;
    private final Clock clock;

    public UserService(UserRepository users, Clock clock) {
        this.users = users;
        this.clock = Objects.requireNonNullElseGet(clock, Clock::systemUTC);
    }

    public UserDocument getOrThrow(UUID userId) {
        return users.findById(userId)
                .orElseThrow(() -> new UserAccessException("USER_NOT_FOUND", "User not found: " + userId));
    }

    @Transactional
    public void validateAndConsumeQuota(UUID userId) {
        var u = getOrThrow(userId);
        if (!u.isActive()) throw new UserAccessException("USER_INACTIVE", "User inactive: " + userId);

        Instant now = clock.instant();
        LocalDate today = LocalDate.ofInstant(now, ZoneOffset.UTC);

        if (u.getLastUsageResetAt() == null ||
                !LocalDate.ofInstant(u.getLastUsageResetAt(), ZoneOffset.UTC).equals(today)) {
            u.setUsedToday(0);
            u.setLastUsageResetAt(today.atStartOfDay().toInstant(ZoneOffset.UTC));
        }

        int limit = Optional.ofNullable(u.getDailyLimit()).orElse(Integer.MAX_VALUE);
        int used = Optional.ofNullable(u.getUsedToday()).orElse(0);

        if (used >= limit) throw new UserAccessException("QUOTA_EXCEEDED", "Daily quota exceeded");

        u.setUsedToday(used + 1);
        users.save(u);
    }
}