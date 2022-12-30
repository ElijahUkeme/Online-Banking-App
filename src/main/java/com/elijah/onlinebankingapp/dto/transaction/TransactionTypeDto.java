package com.elijah.onlinebankingapp.dto.transaction;

import com.elijah.onlinebankingapp.model.account.BankAccount;
import com.elijah.onlinebankingapp.model.customer.Customer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@NoArgsConstructor
public class TransactionTypeDto {

    private double amount;
    private String description;
    private String depositorOrWithDrawalName;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDepositorOrWithDrawalName() {
        return depositorOrWithDrawalName;
    }

    public void setDepositorOrWithDrawalName(String depositorOrWithDrawalName) {
        this.depositorOrWithDrawalName = depositorOrWithDrawalName;
    }
}
