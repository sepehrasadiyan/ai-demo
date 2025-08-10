package org.test.Ai_demo.services.preprocess.pipline;

import java.util.List;
import org.springframework.stereotype.Component;
import org.test.Ai_demo.models.dto.SummarizationInput;
import org.test.Ai_demo.services.preprocess.TextPreprocessor;

@Component
public class PreprocessPipeline {
    private final List<TextPreprocessor> steps;
    public PreprocessPipeline(List<TextPreprocessor> steps) { this.steps = steps; }

    public SummarizationInput run(SummarizationInput input) {
        var text = input.text();
        for (var s : steps) text = s.process(text);
        return new SummarizationInput(text, input.settings());
    }
}
