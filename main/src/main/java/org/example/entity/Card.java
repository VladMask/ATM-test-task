package org.example.entity;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    private long number;
    private String password;
    private BigDecimal balance;
}
