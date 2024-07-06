package org.example.dialog;

import org.example.entity.Card;
import org.example.exception.IllegalAmountException;
import org.example.repository.CardRepository;
import org.example.repository.impl.CardRepositoryImpl;
import org.example.service.CardService;
import org.example.util.CardFileProcessor;

import java.math.BigDecimal;
import java.util.InputMismatchException;
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
        runDialog();
    }

    private String getCardNumberFromUser(){
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("Enter the card number according to the template");
        System.out.println("ХХХХ-ХХХХ-ХХХХ-ХХХХ");
        return scanner.nextLine().trim();
    }

    private boolean isCardPresent(String cardNumber){
        return cardRepository.containsCard(cardNumber);
    }

    private String getPasswordFromUser(){
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("Enter your password");
        return scanner.nextLine().trim();
    }

    private boolean isCardNumberValid(String cardNumber){
        return Pattern.matches("\\d{4}-\\d{4}-\\d{4}-\\d{4}", cardNumber);
    }

    private boolean isAmountValid(String strAmount){
        if(strAmount.contains(".") || strAmount.contains(","))
            return Pattern.matches("\\d+[.,]\\d{2}", strAmount);
        return true;
    }

    private BigDecimal getAmountFromUser(){
        try {
            String strAmount = scanner.nextLine();
            if(!isAmountValid(strAmount)) {
                System.out.println("Please enter a number with only 2 digits after the point");
                return null;
            }
            return new BigDecimal(strAmount);
        }
        catch (NumberFormatException exception){
            System.out.println("Please enter a number or use '.' as a separator");
            return null;
        }
    }

    private void runDialog(){
        String cardNumber;
        String password;
        byte attempts = 3;
        while (true){
            cardNumber = getCardNumberFromUser();
            if(isCardNumberValid(cardNumber)){
                if(isCardPresent(cardNumber)) {
                    Card card = cardRepository.findByCardNumber(cardNumber);
                    if(card.isActive()) {
                        while (attempts > 0) {
                            password = getPasswordFromUser();
                            attempts--;
                            if (password.equals(card.getPassword())) {
                                cardService.setCurrentCard(card);
                                showMenu();
                            } else {
                                System.out.printf("Wrong password. Attempts left: %s%n", attempts);
                            }
                        }
                        System.out.println("Your card was frozen for 24 hours");
                        cardRepository.freezeCard(cardNumber);
                        quit();
                    }
                    else {
                        System.out.println("Sorry, your card is frozen");
                        quit();
                    }
                }
                else{
                    System.out.println("Error. Card was not found");
                    System.out.println("Card number: " + cardNumber);
                    System.out.println("Please try again\n");
                }

            }
            else {
                System.out.println("Invalid input of card number.\n");
            }
        }

    }
    private void showMenu(){
        String choice;
        do{
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("Enter 1 to check balance");
            System.out.println("Enter 2 to withdraw money");
            System.out.println("Enter 3 to deposit money");
            System.out.println("Enter 4 to quit");
            choice = scanner.nextLine();
            switch (choice) {
                case "1" :
                    checkBalance();
                    break;
                case "2" :
                    withdraw();
                    break;
                case "3" :
                    deposit();
                    break;
                case "4" :
                    quit();
                    break;
                default :
                    System.out.println("Unknown command. Please, try again");
                    break;
            }
        }
        while (true);
    }
    private void checkBalance(){
        String choice = "";
        while(!choice.equals("0")) {
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("Enter 0 to go back to menu\n");
            System.out.println("Current balance: " + cardService.checkBalance());
            choice = scanner.nextLine();
        }
    }
    private void withdraw(){
        BigDecimal amount;
        String choice = "";
        while(!choice.equals("0")) {
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("Current balance: " + cardService.checkBalance());
            System.out.println("Enter amount of money you want to withdraw");
            amount = getAmountFromUser();
            if(amount == null)
                continue;
            try {
                cardService.withdraw(amount);
                System.out.println("Operation was performed successfully");
                System.out.println("Current balance: " + cardService.checkBalance());
            }
            catch (IllegalAmountException exception){
                System.out.println(exception);
            }
            System.out.println("\nEnter 0 to go back to menu");
            System.out.println("Enter anything to to continue\n");
            choice = scanner.nextLine();
        }
    }
    private void deposit(){
        BigDecimal amount;
        String choice = "";
        while(!choice.equals("0")) {
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("Current balance: " + cardService.checkBalance());
            System.out.println("Enter amount of money you want to deposit");
            amount = getAmountFromUser();
            if(amount == null)
                continue;
            try {
                cardService.deposit(amount);
                System.out.println("Operation was performed successfully");
                System.out.println("Current balance: " + cardService.checkBalance());
            }
            catch (IllegalAmountException exception){
                System.out.println(exception);
            }
            System.out.println("\nEnter 0 to go back to menu");
            System.out.println("Enter anything to to continue\n");
            choice = scanner.nextLine();
        }
    }
    private void quit(){
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("Application quit");
        CardFileProcessor.update(cardRepository.getAllCards(), dataFile);
        System.exit(0);
    }
}
