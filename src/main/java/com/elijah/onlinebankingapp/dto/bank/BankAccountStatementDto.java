package com.elijah.onlinebankingapp.dto.bank;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class BankAccountStatementDto {
    private Date startDate;
    private Date endDate;
}
