package com.abdou.microblogging.common.exception;

public class PasswordIsMissingException extends RuntimeException {
    public PasswordIsMissingException(String message) {
        super(message);
    }
}
