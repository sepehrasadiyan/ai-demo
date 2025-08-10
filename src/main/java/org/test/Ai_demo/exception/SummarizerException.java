package org.test.Ai_demo.exception;

public class SummarizerException extends RuntimeException {
    private final String code;

    public SummarizerException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String code() {
        return code;
    }
}