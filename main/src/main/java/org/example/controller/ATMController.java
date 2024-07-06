package org.example.controller;

import org.example.entity.ATM;
import org.example.entity.Card;
import org.example.exception.IllegalAmountException;
import org.example.repository.CardRepository;
import org.example.repository.impl.CardRepositoryImpl;
import org.example.service.CardService;
import org.example.service.impl.CardServiceImpl;
import org.example.util.ATMFileProcessor;
import org.example.util.CardFileProcessor;
import org.example.util.StringConstants;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ATMController {

    private final ATM atm;
    private final CardService cardService;
    private final CardRepository cardRepository;
    private final Scanner scanner = new Scanner(System.in);
    public ATMController(){
        this.cardRepository = new CardRepositoryImpl();
        this.cardService = new CardServiceImpl(cardRepository);
        this.atm = new ATM();
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
    private boolean isCardNumberValid(String cardNumber){
        return Pattern.matches("\\d{4}-\\d{4}-\\d{4}-\\d{4}", cardNumber);
    }
    private boolean isCardPresent(String cardNumber){
        return cardRepository.containsCard(cardNumber);
    }
    private boolean isCardFrozen(Card card){
        LocalDateTime dateTime = LocalDateTime.now();
        if(!card.isActive()) {
            if (dateTime.minusHours(24).isAfter(card.getFreezeDate())) {
                cardRepository.defrostCard(card.getNumber());
            }
            else
                return false;
        }
        return true;
    }
    private String getPasswordFromUser(){
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("Enter your password");
        return scanner.nextLine().trim();
    }
    private BigDecimal getAmountFromUser(){
        try {
            String strAmount = scanner.nextLine();
            if(strAmount.contains(","))
                strAmount = strAmount.replace(",", ".");
            if(!isAmountValid(strAmount)) {
                System.out.println("Please enter a number with only 2 digits after the point");
                return null;
            }
            return new BigDecimal(strAmount);
        }
        catch (NumberFormatException exception){
            System.out.println("Please enter a number.");
            return null;
        }
    }
    private boolean isAmountValid(String strAmount){
        if(strAmount.contains("."))
            return Pattern.matches("\\d+[.]\\d{2}", strAmount);
        return true;
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
                    if(isCardFrozen(card)) {
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
            if(amount != null) {
                if(amount.compareTo(atm.getMoneyResource()) < 0) {
                    try {
                        cardService.withdraw(amount);
                        this.atm.withdraw(amount);
                        System.out.println("Operation was performed successfully");
                        System.out.println("Current balance: " + cardService.checkBalance());
                    } catch (IllegalAmountException exception) {
                        System.out.println(exception);
                    }
                    System.out.println("\nEnter 0 to go back to menu");
                    System.out.println("Enter anything to to continue\n");
                    choice = scanner.nextLine();
                }
                else {
                    System.out.printf("Sorry, you cant withdraw more than %s%n", atm.getMoneyResource());
                }
            }
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
                this.atm.deposit(amount);
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
        CardFileProcessor.updateCards(cardRepository.getAllCards(), StringConstants.CARDS_FILE_PATH);
        ATMFileProcessor.updateMoneyResource(atm.getMoneyResource(), StringConstants.ATM_MONEY_RECOURSE_FILE_PATH);
        System.exit(0);
    }
}
