package org.example.service.impl;

import org.example.entity.Card;
import org.example.repository.CardRepository;
import org.example.service.CardService;

import java.math.BigDecimal;

public class CardServiceImpl implements CardService {

    private CardRepository cardRepository;

    public CardServiceImpl(CardRepository cardRepository){
        this.cardRepository = cardRepository;
    }
    @Override
    public BigDecimal checkBalance(String cardNumber) {
        return cardRepository.findByCardNumber(cardNumber).getBalance();
    }

    @Override
    public void withdraw() {
        
    }

    @Override
    public void deposit(BigDecimal amount) {

    }
}
