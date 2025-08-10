package org.test.Ai_demo.models.document;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;


@Document("users")
@CompoundIndexes({
        @CompoundIndex(name = "users_active_plan", def = "{ 'active': 1, 'plan': 1 }")
})
public class UserDocument {

    @Id
    private UUID id;

    @Indexed(name = "users_active_idx")
    private boolean active = true;

    @Indexed(name = "users_plan_idx")
    private String plan;

    private Integer dailyLimit;
    private Integer usedToday;
    private Instant lastUsageResetAt;

    @Version
    private Long version;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public Integer getDailyLimit() {
        return dailyLimit;
    }

    public void setDailyLimit(Integer dailyLimit) {
        this.dailyLimit = dailyLimit;
    }

    public Integer getUsedToday() {
        return usedToday;
    }

    public void setUsedToday(Integer usedToday) {
        this.usedToday = usedToday;
    }

    public Instant getLastUsageResetAt() {
        return lastUsageResetAt;
    }

    public void setLastUsageResetAt(Instant lastUsageResetAt) {
        this.lastUsageResetAt = lastUsageResetAt;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
