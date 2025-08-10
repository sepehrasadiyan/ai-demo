package org.test.Ai_demo.services.preprocess;

import org.test.Ai_demo.models.dto.SummarizationInput;

public interface TextPreprocessor {
    String process(String text);

    default SummarizationInput apply(SummarizationInput in) {
        return new SummarizationInput(process(in.text()), in.settings());
    }
}
