package org.example.service;

import java.math.BigDecimal;

public interface CardService {
    BigDecimal checkBalance(long cardNumber);
    void withdraw();
    void deposit(BigDecimal amount);
}
