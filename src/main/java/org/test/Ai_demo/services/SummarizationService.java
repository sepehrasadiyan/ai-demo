package org.test.Ai_demo.services;

import org.springframework.stereotype.Service;
import org.test.Ai_demo.enums.ProviderId;
import org.test.Ai_demo.models.dto.SummarizationInput;
import org.test.Ai_demo.models.dto.SummarizationOutput;
import org.test.Ai_demo.models.dto.SummarizationSettings;
import org.test.Ai_demo.services.preprocess.pipline.PreprocessPipeline;
import org.test.Ai_demo.services.summarizer.registryport.decorators.DecoratorComposer;
import org.test.Ai_demo.services.summarizer.registryport.SelectionPolicy;

@Service
public class SummarizationService {

    private final SelectionPolicy selection;
    private final PreprocessPipeline pipeline;
    private final DecoratorComposer composer;

    public SummarizationService(SelectionPolicy selection,
                                PreprocessPipeline pipeline,
                                DecoratorComposer composer) {
        this.selection = selection;
        this.pipeline = pipeline;
        this.composer = composer;
    }

    public SummarizationOutput execute(String text, SummarizationSettings settings, ProviderId prefer) {
        var input = pipeline.run(new SummarizationInput(text, settings));

        var base = selection.choose(prefer);
        var chain = composer.compose(base, base.id());

        return chain.summarize(input);
    }
}
