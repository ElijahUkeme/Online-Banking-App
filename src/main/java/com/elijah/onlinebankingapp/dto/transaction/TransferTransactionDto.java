package com.elijah.onlinebankingapp.dto.transaction;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
public class TransferTransactionDto {

    private String accountNumberFrom;
    private String cardNumber;
    private String cardExpiringDate;
    private String cardSecretPin;
    private double amount;

}
