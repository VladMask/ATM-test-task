package org.example.repository.impl;

import org.example.entity.Card;
import org.example.exception.CardNotFoundException;
import org.example.repository.CardRepository;
import org.example.util.CardFileProcessor;
import org.example.util.StringConstants;

import java.time.LocalDateTime;
import java.util.*;

public class CardRepositoryImpl implements CardRepository {
    private final Map<String, Card> cards;
    private static volatile CardRepositoryImpl instance = null;
    private static final Object mutex = new Object();

    private CardRepositoryImpl() {
        this.cards = CardFileProcessor.getAllCards(StringConstants.CARDS_FILE_PATH);
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
        cards.put(entity.getNumber(), entity);
    }

    @Override
    public void update(String cardNumber, Card entity) {
        cards.replace(cardNumber, entity);
    }

    @Override
    public void delete(Card entity) {
        cards.remove(entity.getNumber());
    }

    @Override
    public Card findByCardNumber(String cardNumber) throws CardNotFoundException {
        if(cards.containsKey(cardNumber))
            return cards.get(cardNumber);
        else {
            Map<String, String> details = new HashMap<>();
            details.put("card number", cardNumber);
            throw new CardNotFoundException(
                    CardNotFoundException.class.getSimpleName() + ": Card with specified number wasn't found"
                    , details);
        }
    }

    @Override
    public List<Card> getAllCards() {
        return new ArrayList<>(cards.values());
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

}
