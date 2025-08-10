package org.test.Ai_demo.services.summarizer.registryport;

import org.springframework.stereotype.Component;
import org.test.Ai_demo.enums.ProviderId;
import org.test.Ai_demo.services.summarizer.SummarizerPort;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class SummarizerRegistry {
    private final Map<ProviderId, SummarizerPort> byId = new EnumMap<>(ProviderId.class);

    public SummarizerRegistry(List<SummarizerPort> providers) {
        for (var p : providers) {
            byId.put(p.id(), p);
        }
    }

    public SummarizerPort get(ProviderId id) {
        var p = byId.get(id);
        if (p == null) throw new IllegalArgumentException("No summarizer for " + id);
        return p;
    }

    public SummarizerPort firstAvailable() {
        return byId.values().stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("No summarizer providers registered."));
    }
}