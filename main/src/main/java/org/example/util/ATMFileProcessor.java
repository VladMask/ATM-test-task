package org.example.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.math.BigDecimal;

@UtilityClass
public class ATMFileProcessor {
    @SneakyThrows
    public static BigDecimal getMoneyResource(String fileName){
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String strAmount = reader.readLine();
        reader.close();
        return new BigDecimal(strAmount);
    }

    @SneakyThrows
    public static void updateMoneyResource(BigDecimal amount, String fileName){
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(amount.toString());
        writer.close();
    }
}
