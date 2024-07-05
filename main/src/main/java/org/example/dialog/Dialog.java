package org.example.dialog;

import org.example.entity.Card;
import org.example.exception.CardNotFoundException;
import org.example.repository.CardRepository;
import org.example.repository.impl.CardRepositoryImpl;
import org.example.service.CardService;
import org.example.util.CardFileProcessor;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Dialog {

    private final String dataFile = "main/src/main/resources/data.txt";
    private final CardService cardService;
    private final CardRepository cardRepository;

    public Dialog(){
        this.cardRepository = new CardRepositoryImpl(dataFile);
        this.cardService = new CardService(cardRepository);
    }

    public void start(){
        authenticate();
    }

    private void authenticate(){
        Scanner scanner = new Scanner(System.in);
        short attempts = 3;
        System.out.println("enter the card number according to the template");
        System.out.println("ХХХХ-ХХХХ-ХХХХ-ХХХХ");
        while (attempts > 0){
            String password;
            String cardNumber;
            cardNumber = scanner.nextLine().trim();
            if(isCardNumberValid(cardNumber)){
                System.out.println("Enter your password");
                password = scanner.nextLine().trim();
                if(checkPermission(cardNumber,password)){
                    cardService.setCurrentCard(cardRepository.findByCardNumber(cardNumber));
                    showMenu();
                }
            }
            else {
                System.out.println("Invalid input of card number. Please follow the template\nХХХХ-ХХХХ-ХХХХ-ХХХХ");
            }
            attempts--;
        }
    }

    private boolean checkPermission(String cardNumber, String password){
        Card card = null;
        try {
        card = cardRepository.findByCardNumber(cardNumber);
        } catch (CardNotFoundException exception){
            System.out.println(exception);
        }
        if(card != null)
            return card.getPassword().equals(password);
        return false;
    }

    private boolean isCardNumberValid(String cardNumber){
        return Pattern.matches("\\d{4}-\\d{4}-\\d{4}-\\d{4}", cardNumber);
    }

    private void showMenu(){
        Scanner scanner = new Scanner(System.in);
        short choice;
        do{
            System.out.println("Enter 1 to check balance");
            System.out.println("Enter 2 to withdraw money");
            System.out.println("Enter 3 to deposit money");
            System.out.println("Enter 4 to quit");
            choice = scanner.nextShort();
        }
        while (choice != 4);
        quit();
    }

    private void quit(){
        CardFileProcessor.update(cardRepository.getAllCards(), dataFile);
    }
}
