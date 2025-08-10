package org.test.Ai_demo.services.preprocess.impl;

import org.springframework.stereotype.Component;
import org.test.Ai_demo.services.preprocess.TextPreprocessor;

@Component
public class ClampLengthPreprocessor implements TextPreprocessor {
    private static final int MAX_CHARS = 100_000;
    public String process(String text) {
        if (text == null) return "";
        return text.length() <= MAX_CHARS ? text : text.substring(0, MAX_CHARS);
    }
}
