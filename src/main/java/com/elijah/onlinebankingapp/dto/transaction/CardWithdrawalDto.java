package com.elijah.onlinebankingapp.dto.transaction;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
public class CardWithdrawalDto {
    private double amount;
    private String cardNumber;
    private String cardExpiringDate;
    private String cardSecretPin;


}
