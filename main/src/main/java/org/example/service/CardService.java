package org.example.service;

import org.example.entity.Card;
import org.example.repository.CardRepository;

import java.math.BigDecimal;

public class CardService {
    private Card currentCard;
    private CardRepository cardRepository;
    public CardService(CardRepository cardRepository){
        this.cardRepository = cardRepository;
    }

    public void setCurrentCard(Card currentCard) {
        this.currentCard = currentCard;
    }

    public BigDecimal checkBalance() {
        return currentCard.getBalance();
    }
    public void withdraw(BigDecimal amount) {
        currentCard.setBalance(currentCard.getBalance().subtract(amount));
        cardRepository.update(currentCard.getNumber(), currentCard);
    }
    public void deposit(BigDecimal amount) {
        currentCard.setBalance(currentCard.getBalance().add(amount));
        cardRepository.update(currentCard.getNumber(), currentCard);
    }
}
