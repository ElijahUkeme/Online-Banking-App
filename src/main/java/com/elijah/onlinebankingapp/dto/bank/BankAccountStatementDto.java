package com.elijah.onlinebankingapp.dto.bank;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

@NoArgsConstructor
@Data
public class BankAccountStatementDto {


    private LocalDate startDate;
    private LocalDate endDate;

}
