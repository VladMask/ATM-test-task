package org.example.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.example.entity.Card;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class CardFileProcessor {
    @SneakyThrows
    public static List<Card> getAllCards(String fileName){
        List<Card> cardList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = reader.readLine()) != null){
            String[] args = line.split(" ");
            Card card = Card.builder()
                    .number(args[0])
                    .password(args[1])
                    .balance(new BigDecimal(args[2]))
                    .isActive(Boolean.parseBoolean(args[3]))
                    .build();
            if(card.isActive())
                card.setFreezeDate(null);
            else
                card.setFreezeDate(LocalDateTime.parse(args[4]));
            cardList.add(card);
        }
        reader.close();
        return cardList;
    }

    @SneakyThrows
    public static void updateCards(List<Card> cards, String fileName){
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        for (Card c: cards) {
            String line = c.getNumber() + " "
                    + c.getPassword() + " "
                    + c.getBalance() + " "
                    + c.isActive() + " "
                    + c.getFreezeDate() + "\n";
            writer.write(line);
        }
        writer.close();
    }
}
