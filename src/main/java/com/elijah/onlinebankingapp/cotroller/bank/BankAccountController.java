package com.elijah.onlinebankingapp.cotroller.bank;

import com.elijah.onlinebankingapp.dto.bank.BankAccountDto;
import com.elijah.onlinebankingapp.exception.DataAlreadyExistException;
import com.elijah.onlinebankingapp.exception.DataNotAcceptableException;
import com.elijah.onlinebankingapp.exception.DataNotFoundException;
import com.elijah.onlinebankingapp.model.account.BankAccount;
import com.elijah.onlinebankingapp.response.customer.SignUpResponse;
import com.elijah.onlinebankingapp.service.bank.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;

    @PostMapping("/account/create")
    public ResponseEntity<SignUpResponse> createAccount(@RequestBody BankAccountDto bankAccountDto, @RequestParam("email") String email) throws DataNotFoundException, DataNotAcceptableException, DataAlreadyExistException {
        return bankAccountService.createAccount(bankAccountDto,email);
    }
    @PostMapping("/account/info/through/email")
    public ResponseEntity<BankAccount> getAccountByCustomerEmail(@RequestParam("email") String email) throws DataNotFoundException {
        return new ResponseEntity<>(bankAccountService.getAccountByCustomerEmail(email), HttpStatus.OK);
    }
    @PostMapping("/account/info/through/phoneNumber")
    public ResponseEntity<BankAccount> getAccountByCustomerPhone(@RequestParam("phoneNumber")String phoneNumber) throws DataNotFoundException {
        return new ResponseEntity<>(bankAccountService.getAccountByCustomerPhone(phoneNumber),HttpStatus.OK);
    }
    @PostMapping("/account/info/through/accountNumber")
    public ResponseEntity<BankAccount> getAccountByAccountNumber(@RequestParam("accountNumber") String accountNumber) throws DataNotFoundException {
        return new ResponseEntity<>(bankAccountService.getAccountByAccountNumber(accountNumber),HttpStatus.OK);
    }
}
