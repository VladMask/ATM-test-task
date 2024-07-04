package org.example.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class AbstractException extends RuntimeException {

    private String message;

    private Map<String, String> details;

}
