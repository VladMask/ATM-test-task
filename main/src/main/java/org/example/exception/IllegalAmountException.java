package org.example.exception;

import java.util.Map;

public class IllegalAmountException extends AbstractException{
    public IllegalAmountException(String message, Map<String, String> details) {
        super(message, details);
    }
}
