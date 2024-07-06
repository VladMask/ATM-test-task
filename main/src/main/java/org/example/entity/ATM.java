package org.example.entity;

import lombok.*;
import org.example.util.ATMFileProcessor;
import org.example.util.StringConstants;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ATM {
    private BigDecimal moneyResource;
    public void withdraw(BigDecimal amount){
        this.moneyResource = moneyResource.subtract(amount);
    }

    public void deposit(BigDecimal amount){
        this.moneyResource = moneyResource.add(amount);
    }

    public ATM(){
        this.moneyResource = ATMFileProcessor.getMoneyResource(StringConstants.ATM_MONEY_RECOURSE_FILE_PATH);
    }
}
