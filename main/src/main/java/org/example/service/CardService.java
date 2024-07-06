package org.example.service;

import org.example.entity.Card;
import org.example.exception.IllegalAmountException;
import org.example.repository.CardRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class CardService {
    private Card currentCard;
    private final CardRepository cardRepository;
    public CardService(CardRepository cardRepository){
        this.cardRepository = cardRepository;
    }

    public void setCurrentCard(Card currentCard) {
        this.currentCard = currentCard;
    }

    public BigDecimal checkBalance() {
        return currentCard.getBalance();
    }
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
