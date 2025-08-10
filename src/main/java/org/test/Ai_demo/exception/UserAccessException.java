package org.test.Ai_demo.exception;

public class UserAccessException extends RuntimeException {
    private final String code;

    public UserAccessException(String code, String msg) {
        super(msg);
        this.code = code;
    }

    public String code() {
        return code;
    }
}