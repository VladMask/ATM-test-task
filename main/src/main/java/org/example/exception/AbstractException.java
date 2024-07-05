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
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(message).append("\n");
        for (Map.Entry<String, String> entry : details.entrySet()) {
            sb.append(entry.getKey()).append(" : ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
}
