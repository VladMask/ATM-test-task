package org.example.repository.impl;

import org.example.entity.Card;
import org.example.exception.CardNotFoundException;
import org.example.repository.CardRepository;
import org.example.util.CardFileProcessor;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CardRepositoryImpl implements CardRepository {

    private final List<Card> cardList;
    public CardRepositoryImpl(String fileName){
        this.cardList = CardFileProcessor.getAllCards(fileName);
    }
    @Override
    public void insert(Card entity) {
        cardList.add(entity);
    }

    @Override
    public void update(String cardNumber, Card entity) {
        int index = cardList.indexOf(
                cardList.stream().filter(card -> card.getNumber().equals(cardNumber)).findFirst().get()
        );
        cardList.set(index, entity);
    }

    @Override
    public void delete(Card entity) {
        cardList.remove(entity);
    }

    @Override
    public Card findByCardNumber(String cardNumber) {
        Optional<Card> result = cardList.stream().filter(card -> card.getNumber().equals(cardNumber)).findFirst();
        if(result.isPresent())
            return result.get();
        else
            throw new CardNotFoundException("Card was not found", Map.of("card number",cardNumber));
    }
}
