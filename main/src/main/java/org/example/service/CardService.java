package org.example.service;

import java.math.BigDecimal;

public interface CardService {
    BigDecimal checkBalance(String cardNumber);
    void withdraw();
    void deposit(BigDecimal amount);
}
