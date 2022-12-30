package com.elijah.onlinebankingapp.dto.card;


import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Random;

@NoArgsConstructor
public class DebitCardDto {

    private String cardSecretPin;
    private String cardType;

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public LocalDate getExpiringDate() {
        LocalDate localDate = LocalDate.now();
        LocalDate returnDate = localDate.plusYears(3);
        return returnDate;
    }


    public String getCardNumber() {
        return getDebitCardNumber();
    }

    public String getCardSecretPin() {
        return cardSecretPin;
    }

    public void setCardSecretPin(String cardSecretPin) {
        this.cardSecretPin = cardSecretPin;
    }
    private String getDebitCardNumber(){
        Random random = new Random();
        String generatedCardNumber="";
        String card = "EL";
        int r1 = random.nextInt(10);
        int r2 = random.nextInt(10);
        card +=Integer.toString(r1)+ Integer.toString(r2)+" ";
        int count = 0;
        int n = 0;
        for (int i=0;i<12;i++) {
            if (count == 4) {
                card += " ";
                count = 0;
            } else
                n = random.nextInt(10);
            card += Integer.toString(n);
            count++;
            generatedCardNumber = card;
        }
        return generatedCardNumber;
    }
}
