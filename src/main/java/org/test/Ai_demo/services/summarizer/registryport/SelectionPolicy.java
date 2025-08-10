package org.test.Ai_demo.services.summarizer.registryport;

import org.springframework.stereotype.Component;
import org.test.Ai_demo.config.properties.SummarizerProperties;
import org.test.Ai_demo.enums.ProviderId;
import org.test.Ai_demo.services.summarizer.SummarizerPort;

@Component
public class SelectionPolicy {
    private final SummarizerRegistry registry;
    private final ProviderId defaultProvider;

    public SelectionPolicy(SummarizerRegistry registry, SummarizerProperties props) {
        this.registry = registry;
        this.defaultProvider = props.defaultProvider();
    }

    public SummarizerPort choose(ProviderId requested) {
        if (requested != null) return registry.get(requested);
        if (defaultProvider != null) return registry.get(defaultProvider);
        return registry.firstAvailable();
    }
}