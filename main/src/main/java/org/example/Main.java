package org.example;

import org.example.entity.Card;
import org.example.util.CardFileProcessor;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        short attempts = 3;
        while (attempts > 0){
            System.out.println("enter the card number according to the template");
            System.out.println("ХХХХ-ХХХХ-ХХХХ-ХХХХ");
            String line = scanner.nextLine();
            // todo
            // check steps
            // logs
            attempts--;
        }
//        List<Card> cardList = CardFileProcessor.getAllCards("main/src/main/resources/data.txt");
//        for(Card c: cardList){
//            System.out.println(c.getNumber());
//        }
    }
}