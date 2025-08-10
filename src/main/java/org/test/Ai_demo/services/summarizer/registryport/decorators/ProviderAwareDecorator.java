package org.test.Ai_demo.services.summarizer.registryport.decorators;

import org.test.Ai_demo.enums.ProviderId;
import org.test.Ai_demo.services.summarizer.SummarizerPort;

public interface ProviderAwareDecorator {
    boolean appliesTo(ProviderId providerId);

    SummarizerPort wrap(SummarizerPort delegate);
}
