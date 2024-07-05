package org.example.exception;

import java.util.Map;

public class InvalidCardNumberException extends AbstractException {

    public InvalidCardNumberException(String message, Map<String, String> details) {
        super(message, details);
    }
}
