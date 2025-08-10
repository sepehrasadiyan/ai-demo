package org.test.Ai_demo.services.preprocess.impl;

import org.springframework.stereotype.Component;
import org.test.Ai_demo.services.preprocess.TextPreprocessor;

@Component
public class NormalizeWhitespacePreprocessor implements TextPreprocessor {
    @Override
    public String process(String text) {
        return text == null ? "" : text.replaceAll("\\s+", " ").trim();
    }

}