package com.elijah.onlinebankingapp.model.account;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BankAccountStatement {
    private double id;
    private String transactionDate;
    private String transactionType;
    private String description;
    private double amount;
    private double currentBalance;
    private String depositorOrWithDrawalName;
}
