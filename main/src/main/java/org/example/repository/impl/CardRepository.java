package org.example.repository.impl;

import org.example.entity.Card;
import org.example.repository.CrudRepository;

import java.math.BigDecimal;

public class CardRepository implements CrudRepository<Card, BigDecimal> {
    @Override
    public void insert(Card entity) {

    }

    @Override
    public void update(Card entity) {

    }

    @Override
    public void delete(Card entity) {

    }

    @Override
    public Card findById(BigDecimal bigDecimal) {
        return null;
    }
}
