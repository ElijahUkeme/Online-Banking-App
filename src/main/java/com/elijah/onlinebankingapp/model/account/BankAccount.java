package com.elijah.onlinebankingapp.model.account;


import com.elijah.onlinebankingapp.model.customer.Customer;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.juli.logging.Log;
import org.slf4j.Logger;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

@Entity
@Data
@NoArgsConstructor
@Slf4j
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountType;
    private String accountStatus;
    private String accountNumber;
    private double currentBalance;
    private LocalDate createdDate;
    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
