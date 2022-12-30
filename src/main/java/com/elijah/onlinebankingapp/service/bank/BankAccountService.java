package com.elijah.onlinebankingapp.service.bank;


import com.elijah.onlinebankingapp.dto.bank.BankAccountDto;
import com.elijah.onlinebankingapp.exception.DataAlreadyExistException;
import com.elijah.onlinebankingapp.exception.DataNotAcceptableException;
import com.elijah.onlinebankingapp.exception.DataNotFoundException;
import com.elijah.onlinebankingapp.model.account.BankAccount;
import com.elijah.onlinebankingapp.model.customer.Customer;
import com.elijah.onlinebankingapp.repository.bank.BankAccountRepository;
import com.elijah.onlinebankingapp.response.customer.SignUpResponse;
import com.elijah.onlinebankingapp.service.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;

@Service
public class BankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private CustomerService customerService;

    public ResponseEntity<SignUpResponse> createAccount(BankAccountDto bankAccountDto, String customerEmail) throws DataNotAcceptableException, DataNotFoundException, DataAlreadyExistException {
        Customer customer = customerService.getCustomerByEmail(customerEmail);
        BankAccount bankAccount = bankAccountRepository.findByCustomer(customer);
        if (Objects.nonNull(bankAccount)){
                throw new DataAlreadyExistException("This customer has already created an Account");
        }
        BankAccount account = bankAccountRepository.findByAccountNumber(bankAccountDto.getAccountNumber());
        if (Objects.nonNull(account)){
            throw new DataAlreadyExistException("This Account Number has already been generated to Another Customer, Please Try Again");
        }
        BankAccount createdAccount = new BankAccount();
        createdAccount.setAccountNumber(bankAccountDto.getAccountNumber());
        createdAccount.setAccountStatus(bankAccountDto.getAccountStatus());
        createdAccount.setAccountType(bankAccountDto.getAccountType());
        createdAccount.setCreatedDate(LocalDate.now());
        createdAccount.setCustomer(customer);
        if (bankAccountDto.getCurrentBalance()<2000){
            throw new DataNotAcceptableException("You can't open an account with less than 2000");

        }else {
            createdAccount.setCurrentBalance(bankAccountDto.getCurrentBalance());
        }
        bankAccountRepository.save(createdAccount);
        return new ResponseEntity<>(new SignUpResponse(true,"Account created Successfully"), HttpStatus.CREATED);
    }
    public BankAccount getAccountByCustomerEmail(String customerEmail) throws DataNotFoundException {
        Customer customer = customerService.getCustomerByEmail(customerEmail);
        BankAccount bankAccount = bankAccountRepository.findByCustomer(customer);
        if (Objects.isNull(bankAccount)){
            throw new DataNotFoundException("Sorry, there is no such customer in our database");
        }
        return bankAccount;
    }
    public BankAccount getAccountByCustomerPhone(String phoneNumber) throws DataNotFoundException {
        Customer customer = customerService.getCustomerByPhoneNumber(phoneNumber);
        BankAccount bankAccount = bankAccountRepository.findByCustomer(customer);
        if (Objects.isNull(bankAccount)){
            throw new DataNotFoundException("Sorry, there is no such customer in our database");
        }
        return bankAccount;
    }
    public BankAccount getAccountByAccountNumber(String accountNumber) throws DataNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findByAccountNumber(accountNumber);
        if (Objects.isNull(bankAccount)){
            throw new DataNotFoundException("The Account Number is not valid");
        }
        return bankAccount;
    }
    public BankAccount updateAccountInformation(BankAccountDto bankAccountDto, String accountNumber) throws DataNotFoundException {
        BankAccount bankAccount = getAccountByAccountNumber(accountNumber);
        if (Objects.nonNull(bankAccountDto.getAccountType())&& !"".equals(bankAccountDto.getAccountType())){
            bankAccount.setAccountType(bankAccountDto.getAccountType());
        }if (Objects.nonNull(bankAccountDto.getAccountStatus())&& !"".equals(bankAccountDto.getAccountStatus())){
            bankAccount.setAccountStatus(bankAccountDto.getAccountStatus());
        }
        return bankAccountRepository.save(bankAccount);
    }
    public BankAccount updateAccountBalance(BankAccount bankAccount,double newBalance,String accountNumber) throws DataNotFoundException {
         bankAccount= getAccountByAccountNumber(accountNumber);
        if (Objects.nonNull(newBalance)){
            bankAccount.setCurrentBalance(newBalance);
        }
        return bankAccountRepository.save(bankAccount);
    }
}
