package org.example.repository;

import org.example.entity.Card;

public interface CardRepository {
    void insert(Card entity);
    void update(String cardNumber, Card entity);
    void delete(Card entity);
    Card findByCardNumber(String cardNumber);
}
