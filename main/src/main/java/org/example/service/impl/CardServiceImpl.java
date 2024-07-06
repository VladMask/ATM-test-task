package org.example.service.impl;

import org.example.entity.Card;
import org.example.exception.IllegalAmountException;
import org.example.repository.CardRepository;
import org.example.service.CardService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CardServiceImpl implements CardService {
    private Card currentCard;
    private final CardRepository cardRepository;
    public CardServiceImpl(CardRepository cardRepository){
        this.cardRepository = cardRepository;
    }

    @Override
    public void setCurrentCard(Card currentCard) {
        this.currentCard = currentCard;
    }

    @Override
    public BigDecimal checkBalance() {
        return currentCard.getBalance();
    }
    @Override
    public void withdraw(BigDecimal amount) throws IllegalAmountException {
        if(currentCard.getBalance().compareTo(amount) >= 0 && amount.compareTo(BigDecimal.ZERO) > 0) {
            currentCard.setBalance(currentCard.getBalance().subtract(amount));
            cardRepository.update(currentCard.getNumber(), currentCard);
        }
        else {
            Map<String, String> details = new HashMap<>();
            details.put("Balance",currentCard.getBalance().toString());
            details.put("Amount to withdraw", amount.toString());
            throw new IllegalAmountException("Error. Illegal amount was entered", details);
        }

    }
    @Override
    public void deposit(BigDecimal amount) throws IllegalAmountException {

        if(amount.compareTo(BigDecimal.valueOf(1_000_000)) < 0 && amount.compareTo(BigDecimal.ZERO) > 0) {
            currentCard.setBalance(currentCard.getBalance().add(amount));
            cardRepository.update(currentCard.getNumber(), currentCard);
        }
        else{
            Map<String, String> details = new HashMap<>();
            details.put("Balance",currentCard.getBalance().toString());
            details.put("Amount to deposit", amount.toString());
            throw new IllegalAmountException("Error. Illegal amount was entered", details);
        }
    }
}
