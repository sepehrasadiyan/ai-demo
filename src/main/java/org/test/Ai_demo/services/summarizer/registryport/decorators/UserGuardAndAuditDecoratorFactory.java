package org.test.Ai_demo.services.summarizer.registryport.decorators;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.test.Ai_demo.enums.ProviderId;
import org.test.Ai_demo.exception.SummarizerException;
import org.test.Ai_demo.models.dto.SummarizationInput;
import org.test.Ai_demo.models.dto.SummarizationOutput;
import org.test.Ai_demo.services.history.HistoryService;
import org.test.Ai_demo.services.summarizer.SummarizerPort;
import org.test.Ai_demo.services.user.UserService;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class UserGuardAndAuditDecoratorFactory implements ProviderAwareDecorator {

    private final UserService userService;
    private final HistoryService historyService;

    public UserGuardAndAuditDecoratorFactory(UserService userService, HistoryService historyService) {
        this.userService = userService;
        this.historyService = historyService;
    }

    @Override public boolean appliesTo(ProviderId providerId) { return true; }

    @Override
    public SummarizerPort wrap(SummarizerPort delegate) {
        return new SummarizerPort() {
            @Override public ProviderId id() { return delegate.id(); }

            @Override
            public SummarizationOutput summarize(SummarizationInput input) {
                var userId = input.settings().userId();
                var preview = HistoryService.preview(input.text(), 200);

                userService.validateAndConsumeQuota(userId);

                try {
                    var out = delegate.summarize(input);
                    historyService.recordSuccess(userId, id().name(), out.model(), preview, out.summary());
                    return out;
                } catch (SummarizerException ex) {
                    historyService.recordFailure(userId, id().name(), preview, ex.code(), ex.getMessage());
                    throw ex;
                } catch (RuntimeException ex) {
                    historyService.recordFailure(userId, id().name(), preview, "INTERNAL_ERROR", ex.getMessage());
                    throw ex;
                }
            }
        };
    }
}