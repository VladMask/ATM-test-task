package org.example.service;

import java.math.BigDecimal;

public interface CardService {
    BigDecimal checkBalance();
    void withdraw(BigDecimal amount);
    void deposit(BigDecimal amount);
}
