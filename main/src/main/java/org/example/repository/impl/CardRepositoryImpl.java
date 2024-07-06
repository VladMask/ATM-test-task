package org.example.repository.impl;

import org.example.entity.Card;
import org.example.exception.CardNotFoundException;
import org.example.repository.CardRepository;
import org.example.util.CardFileProcessor;
import org.example.util.StringConstants;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CardRepositoryImpl implements CardRepository {


    //todo
    //SINGLETON
    //private final CardRepositoryImpl cardRepository;
    private final List<Card> cardList;
    public CardRepositoryImpl(){
        this.cardList = CardFileProcessor.getAllCards(StringConstants.CARDS_FILE_PATH);
        LocalDate localDate = LocalDate.now();
        for (Card c: cardList) {
            if(!c.isActive()) {
                if (localDate.minusDays(1).isAfter(c.getFreezeDate()))
                    c.setActive(true);
            }
        }
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
        card.setFreezeDate(LocalDate.now());
        update(cardNumber,card);
    }

    @Override
    public boolean containsCard(String cardNumber) {
        return cardList.stream().anyMatch(card -> card.getNumber().equals(cardNumber));
    }

}
