package org.example.dialog;

import org.example.entity.Card;
import org.example.exception.CardNotFoundException;
import org.example.exception.IllegalAmountException;
import org.example.repository.CardRepository;
import org.example.repository.impl.CardRepositoryImpl;
import org.example.service.CardService;
import org.example.util.CardFileProcessor;

import java.math.BigDecimal;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Dialog {
    private final String dataFile = "main/src/main/resources/data.txt";
    private final CardService cardService;
    private final CardRepository cardRepository;

    private final Scanner scanner = new Scanner(System.in);
    public Dialog(){
        this.cardRepository = new CardRepositoryImpl(dataFile);
        this.cardService = new CardService(cardRepository);
    }
    public void start(){
        authenticate();
    }
    private void authenticate(){
        byte attempts = 3;
        System.out.println("enter the card number according to the template");
        System.out.println("ХХХХ-ХХХХ-ХХХХ-ХХХХ");
        while(true){
            String password;
            String cardNumber;
            cardNumber = scanner.nextLine().trim();
            if(isCardNumberValid(cardNumber)){
                System.out.println("Enter your password");
                while (attempts > 0){
                    password = scanner.nextLine().trim();
                    attempts--;
                    if(checkPermission(cardNumber,password)){
                        cardService.setCurrentCard(cardRepository.findByCardNumber(cardNumber));
                        showMenu();
                    }
                    else {
                        System.out.printf("Wrong password. Attempts left: %s%n", attempts);
                    }
                }

            }
            else {
                System.out.println("Invalid input of card number. Please follow the template\nХХХХ-ХХХХ-ХХХХ-ХХХХ");
            }
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
        byte choice;
        do{
            System.out.println("Enter 1 to check balance");
            System.out.println("Enter 2 to withdraw money");
            System.out.println("Enter 3 to deposit money");
            System.out.println("Enter 4 to quit");
            choice = scanner.nextByte();
            switch (choice) {
                case 1 -> checkBalance();
                case 2 -> withdraw();
                case 3 -> deposit();
            }
        }
        while (choice != 4);
        quit();
    }
    private void checkBalance(){
        byte choice = -1;
        while(choice != 0) {
            System.out.println("Enter 0 to go back to menu\n");
            System.out.println("Current balance: " + cardService.checkBalance());
            choice = scanner.nextByte();
        }
    }
    private void withdraw(){
        byte choice = -1;
        BigDecimal amount;
        while(choice != 0) {
            System.out.println("Enter 0 to go back to menu\n");
            System.out.println("Current balance: " + cardService.checkBalance());
            System.out.println("Enter amount of money you want to withdraw");
            amount = scanner.nextBigDecimal();
            try {
                cardService.withdraw(amount);
                System.out.println("Operation was performed successfully");
                System.out.println("Current balance: " + cardService.checkBalance());
            }
            catch (IllegalAmountException exception){
                System.out.println(exception);
            }
            choice = scanner.nextByte();
        }
    }
    private void deposit(){
        byte choice = -1;
        BigDecimal amount;
        while(choice != 0) {
            System.out.println("Enter 0 to go back to menu\n");
            System.out.println("Current balance: " + cardService.checkBalance());
            System.out.println("Enter amount of money you want to deposit");
            amount = scanner.nextBigDecimal();
            try {
                cardService.deposit(amount);
                System.out.println("Operation was performed successfully");
                System.out.println("Current balance: " + cardService.checkBalance());
            }
            catch (IllegalAmountException exception){
                System.out.println(exception);
            }
            System.out.println("Enter 1 to go back to continue\n");
            choice = scanner.nextByte();
        }
    }
    private void quit(){
        CardFileProcessor.update(cardRepository.getAllCards(), dataFile);
        System.exit(0);
    }
}
