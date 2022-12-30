package com.elijah.onlinebankingapp.cotroller.transaction;

import com.elijah.onlinebankingapp.dto.bank.BankAccountStatementDto;
import com.elijah.onlinebankingapp.dto.transaction.CardWithdrawalDto;
import com.elijah.onlinebankingapp.dto.transaction.TransactionTypeDto;
import com.elijah.onlinebankingapp.dto.transaction.TransferTransactionDto;
import com.elijah.onlinebankingapp.exception.DataNotAcceptableException;
import com.elijah.onlinebankingapp.exception.DataNotFoundException;
import com.elijah.onlinebankingapp.model.account.BankAccountStatement;
import com.elijah.onlinebankingapp.model.transaction.TransactionType;
import com.elijah.onlinebankingapp.response.transaction.TransactionResponse;
import com.elijah.onlinebankingapp.service.transaction.TransactionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
public class TransactionController {

    @Autowired
    private TransactionTypeService transactionTypeService;

    @PostMapping("/transaction/deposit")
    public ResponseEntity<TransactionResponse> depositMoney(@RequestBody TransactionTypeDto transactionTypeDto, @RequestParam("accountNumber") String accountNumber) throws DataNotFoundException, DataNotAcceptableException {
        return transactionTypeService.deposit(transactionTypeDto,accountNumber);
    }
    @PostMapping("/transaction/withdraw/at/counter")
    public ResponseEntity<TransactionResponse> withdrawMoney(@RequestBody TransactionTypeDto transactionTypeDto, @RequestParam("accountNumber") String accountNumber) throws DataNotAcceptableException, DataNotFoundException {
        return transactionTypeService.withdrawAtCounter(transactionTypeDto,accountNumber);
    }
    @PostMapping("/transaction/withdraw/with/card")
    public ResponseEntity<TransactionResponse> cardWithDrawWal(@RequestBody CardWithdrawalDto cardWithdrawalDto, @RequestParam("accountNumber")String accountNumber) throws DataNotAcceptableException, NoSuchAlgorithmException, DataNotFoundException {
        return transactionTypeService.withDrawWithCard(cardWithdrawalDto,accountNumber);
    }
    @GetMapping("/transaction/list")
    public ResponseEntity<List<TransactionType>> getAllTransactions(){
        return new ResponseEntity<>(transactionTypeService.getAllTransaction(), HttpStatus.OK);
    }
    @PostMapping("/transaction/transfer")
    public ResponseEntity<TransactionResponse> transferMoney(@RequestBody TransferTransactionDto transferTransactionDto, @RequestParam("recipientAccountNumber")String recipientAccountNumber) throws DataNotAcceptableException, NoSuchAlgorithmException, DataNotFoundException {
        return transactionTypeService.transferMoneyToAnotherAccount(transferTransactionDto,recipientAccountNumber);
    }
    @GetMapping("/account/statement")
    public ResponseEntity<List<BankAccountStatement>> getAccountStatement(@RequestParam("accountNumber") String accountNumber) throws DataNotFoundException {
        return new ResponseEntity<>(transactionTypeService.getAccountStatementForTheUser(accountNumber),HttpStatus.OK);
    }
    @PostMapping("/account/statement/from/enteredDate")
    public ResponseEntity<List<BankAccountStatement>> getCustomerAccountStatement(@RequestBody BankAccountStatementDto bankAccountStatementDto,@RequestParam("accountNumber")String accountNumber) throws DataNotFoundException {
        return new ResponseEntity<>(transactionTypeService.getUserAccountStatement(bankAccountStatementDto,accountNumber),HttpStatus.OK);
    }
    @PostMapping("/account/from/date")
    public ResponseEntity<List<BankAccountStatement>> allTransactionFromDate(@RequestBody BankAccountStatementDto bankAccountStatementDto){
        return new ResponseEntity<>(transactionTypeService.getAccountStatement(bankAccountStatementDto),HttpStatus.OK);
    }
}
