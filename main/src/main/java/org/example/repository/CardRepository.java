package org.example.repository;

import org.example.entity.Card;

import java.util.List;

public interface CardRepository {
    void insert(Card entity);
    void update(String cardNumber, Card entity);
    void delete(Card entity);
    Card findByCardNumber(String cardNumber);
    List<Card> getAllCards();
    void freezeCard(String cardNumber);
    boolean containsCard(String cardNumber);
}
