package org.test.Ai_demo.config.seed;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.test.Ai_demo.models.document.UserDocument;
import org.test.Ai_demo.repository.UserRepository;

import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Profile("!test")
public class AdminSeed implements ApplicationRunner {

    public AdminSeed(UserRepository users) {
        this.users = users;
    }

    public static final UUID ADMIN_ID = UUID.fromString("8e305566-a042-4a64-a493-b893c27b1ee0");

    private final UserRepository users;

    @Override
    public void run(ApplicationArguments args) {
        users.findById(ADMIN_ID).ifPresentOrElse(existing -> {
            boolean changed = false;

            if (!existing.isActive()) {
                existing.setActive(true);
                changed = true;
            }
            if (existing.getPlan() == null || !"ADMIN".equalsIgnoreCase(existing.getPlan())) {
                existing.setPlan("ADMIN");
                changed = true;
            }
            if (existing.getDailyLimit() == null) {
                existing.setDailyLimit(null);
            }
            if (existing.getUsedToday() == null) {
                existing.setUsedToday(0);
                changed = true;
            }

            if (changed) {
                users.save(existing);
            }
        }, () -> {
            var admin = new UserDocument();
            admin.setId(ADMIN_ID);
            admin.setActive(true);
            admin.setPlan("ADMIN");
            admin.setDailyLimit(null);
            admin.setUsedToday(0);
            users.save(admin);
        });
    }
}
