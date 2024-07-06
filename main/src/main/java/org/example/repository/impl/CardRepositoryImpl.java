package org.example.repository.impl;

import org.example.entity.Card;
import org.example.exception.CardNotFoundException;
import org.example.repository.CardRepository;
import org.example.util.CardFileProcessor;
import org.example.util.StringConstants;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CardRepositoryImpl implements CardRepository {

    private final List<Card> cardList;
    private static volatile CardRepositoryImpl instance = null;
    private static final Object mutex = new Object();

    private CardRepositoryImpl() {
        this.cardList = CardFileProcessor.getAllCards(StringConstants.CARDS_FILE_PATH);
    }
    public static CardRepositoryImpl getInstance() {
        if (instance == null) {
            synchronized (mutex) {
                instance =  new CardRepositoryImpl();
            }
        }
        return instance;
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
    public Card findByCardNumber(String cardNumber) throws CardNotFoundException {
        Optional<Card> result = cardList.stream().filter(card -> card.getNumber().equals(cardNumber)).findFirst();
        if(result.isPresent())
            return result.get();
        else {
            Map<String, String> details = new HashMap<>();
            details.put("card number", cardNumber);
            throw new CardNotFoundException("Error. Card was not found", details);
        }
    }

    @Override
    public List<Card> getAllCards() {
        return this.cardList;
    }

    @Override
    public void freezeCard(String cardNumber) {
        Card card = findByCardNumber(cardNumber);
        card.setActive(false);
        card.setFreezeDate(LocalDateTime.now());
        update(cardNumber,card);
    }

    @Override
    public void defrostCard(String cardNumber) {
        Card card = findByCardNumber(cardNumber);
        card.setActive(true);
        card.setFreezeDate(null);
        update(cardNumber,card);
    }

    @Override
    public boolean containsCard(String cardNumber) {
        return cardList.stream().anyMatch(card -> card.getNumber().equals(cardNumber));
    }

}
