package org.test.Ai_demo.services.summarizer.registryport.decorators;

import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;
import org.test.Ai_demo.enums.ProviderId;
import org.test.Ai_demo.services.summarizer.SummarizerPort;

import java.util.ArrayList;
import java.util.List;

@Component
public class DecoratorComposer {

    private final List<ProviderAwareDecorator> decorators;

    public DecoratorComposer(List<ProviderAwareDecorator> decorators) {
        var ordered = new ArrayList<>(decorators);
        AnnotationAwareOrderComparator.sort(ordered);
        this.decorators = List.copyOf(ordered);
    }

    public SummarizerPort compose(SummarizerPort base, ProviderId id) {
        SummarizerPort chain = base;
        for (ProviderAwareDecorator d : decorators) {
            if (d.appliesTo(id)) {
                chain = d.wrap(chain);// every one that wrap later are load last
            }
        }
        return chain;
    }
}