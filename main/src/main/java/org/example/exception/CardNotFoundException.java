package org.example.exception;

import java.util.Map;

public class CardNotFoundException extends AbstractException {

    public CardNotFoundException(String message, Map<String, String> details) {
        super(message, details);
    }
}
