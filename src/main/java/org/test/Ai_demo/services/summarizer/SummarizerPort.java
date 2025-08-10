package org.test.Ai_demo.services.summarizer;

import org.test.Ai_demo.enums.ProviderId;
import org.test.Ai_demo.exception.SummarizerException;
import org.test.Ai_demo.models.dto.SummarizationInput;
import org.test.Ai_demo.models.dto.SummarizationOutput;

public interface SummarizerPort {
    ProviderId id();
    SummarizationOutput summarize(SummarizationInput input) throws SummarizerException;
}
