package org.example.service.impl;

import org.example.entity.Card;
import org.example.repository.CardRepository;
import org.example.service.CardService;

import java.math.BigDecimal;

public class CardServiceImpl implements CardService {
    private Card currentCard;
    private CardRepository cardRepository;
    public CardServiceImpl(CardRepository cardRepository){
        this.cardRepository = cardRepository;
    }

    public void setCurrentCard(Card currentCard) {
        this.currentCard = currentCard;
    }

    @Override
    public BigDecimal checkBalance() {
        return currentCard.getBalance();
    }

    @Override
    public void withdraw(BigDecimal amount) {
        currentCard.setBalance(currentCard.getBalance().subtract(amount));
        cardRepository.update(currentCard.getNumber(), currentCard);
    }

    @Override
    public void deposit(BigDecimal amount) {
        currentCard.setBalance(currentCard.getBalance().add(amount));
        cardRepository.update(currentCard.getNumber(), currentCard);
    }

}
