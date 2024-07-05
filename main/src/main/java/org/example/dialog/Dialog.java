package org.example.dialog;

import org.example.repository.CardRepository;
import org.example.repository.impl.CardRepositoryImpl;
import org.example.service.CardService;
import org.example.service.impl.CardServiceImpl;

import java.util.Scanner;

public class Dialog {
    private final String dataFile = "main/src/main/resources/data.txt";
    private CardService cardService;

    public Dialog(){
        CardRepository repository = new CardRepositoryImpl(dataFile);
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
            String line = scanner.nextLine();
            // todo
            // check steps
            // logs
            attempts--;
        }
    }

    public void quit(){

    }
}
