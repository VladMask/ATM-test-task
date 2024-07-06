package org.example.service;

import org.example.entity.Card;

import java.math.BigDecimal;

public interface CardService {
    void setCurrentCard(Card currentCard);
    BigDecimal checkBalance();
    void withdraw(BigDecimal amount);
    void deposit(BigDecimal amount);
}
