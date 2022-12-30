package com.elijah.onlinebankingapp.service.transaction;

import com.elijah.onlinebankingapp.dto.bank.BankAccountStatementDto;
import com.elijah.onlinebankingapp.dto.transaction.CardWithdrawalDto;
import com.elijah.onlinebankingapp.dto.transaction.TransactionTypeDto;
import com.elijah.onlinebankingapp.dto.transaction.TransferTransactionDto;
import com.elijah.onlinebankingapp.exception.DataNotAcceptableException;
import com.elijah.onlinebankingapp.exception.DataNotFoundException;
import com.elijah.onlinebankingapp.model.account.BankAccount;
import com.elijah.onlinebankingapp.model.account.BankAccountStatement;
import com.elijah.onlinebankingapp.model.card.DebitCard;
import com.elijah.onlinebankingapp.model.transaction.TransactionType;
import com.elijah.onlinebankingapp.repository.bank.BankAccountRepository;
import com.elijah.onlinebankingapp.repository.transaction.TransactionTypeRepository;
import com.elijah.onlinebankingapp.response.transaction.TransactionResponse;
import com.elijah.onlinebankingapp.service.bank.BankAccountService;
import com.elijah.onlinebankingapp.service.card.DebitCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TransactionTypeService {
    @Autowired
    private TransactionTypeRepository transactionTypeRepository;
    @Autowired
    private BankAccountService bankAccountService;
    @Autowired
    private DebitCardService debitCardService;

    public ResponseEntity<TransactionResponse> deposit(TransactionTypeDto transactionTypeDto, String accountNumber) throws DataNotFoundException, DataNotAcceptableException {
        BankAccount bankAccount = bankAccountService.getAccountByAccountNumber(accountNumber);
        double currentBalance = bankAccount.getCurrentBalance();
        TransactionType transactionType = new TransactionType();
        if (transactionTypeDto.getAmount()<=0){
            throw new DataNotAcceptableException("Amount must be greater than zero");
        }
        double newBalance = currentBalance+transactionTypeDto.getAmount();
        transactionType.setAmount(transactionTypeDto.getAmount());
        transactionType.setBankAccount(bankAccount);
        transactionType.setCurrentBalance(newBalance);
        transactionType.setTransactionType("CREDIT");
        transactionType.setTransactionDate(new Date());
        transactionType.setDescription(transactionTypeDto.getDescription());
        transactionType.setDepositorOrWithDrawalName(transactionTypeDto.getDepositorOrWithDrawalName());
        bankAccountService.updateAccountBalance(bankAccount,newBalance,accountNumber);
        transactionTypeRepository.save(transactionType);
        return new ResponseEntity<>(new TransactionResponse("Deposit","Successful","Your Account "+bankAccount.getAccountNumber()+
                " has been credited with "+transactionTypeDto.getAmount()+" at "+transactionType.getTransactionDate()+" by " +
                transactionTypeDto.getDepositorOrWithDrawalName()+", your current balance is #"+newBalance), HttpStatus.OK);
    }
    public ResponseEntity<TransactionResponse> withdrawAtCounter(TransactionTypeDto transactionTypeDto, String accountNumber) throws DataNotFoundException, DataNotAcceptableException {
        BankAccount bankAccount = bankAccountService.getAccountByAccountNumber(accountNumber);
        double currentBalance = bankAccount.getCurrentBalance();
        if (!checkForInsufficientFund(currentBalance,transactionTypeDto.getAmount())){
            throw new DataNotAcceptableException("Insufficient fund");
        }
        TransactionType transactionType = new TransactionType();
        if (transactionTypeDto.getAmount()<=0){
            throw new DataNotAcceptableException("Amount must be greater than zero");
        }
        double newBalance = currentBalance - transactionTypeDto.getAmount();
        transactionType.setDepositorOrWithDrawalName(transactionTypeDto.getDepositorOrWithDrawalName());
        transactionType.setDescription(transactionTypeDto.getDescription());
        transactionType.setTransactionDate(new Date());
        transactionType.setTransactionType("DEBIT");
        transactionType.setCurrentBalance(newBalance);
        transactionType.setBankAccount(bankAccount);
        transactionType.setAmount(transactionTypeDto.getAmount());
        bankAccountService.updateAccountBalance(bankAccount,newBalance,accountNumber);
        transactionTypeRepository.save(transactionType);
        return new ResponseEntity<>(new TransactionResponse("Withdrawal","Successful","Your Account "+bankAccount.getAccountNumber()+
                " has been debited with "+transactionTypeDto.getAmount()+" at "+transactionType.getTransactionDate()+" by " +
                transactionTypeDto.getDepositorOrWithDrawalName()+", your current balance is #"+newBalance), HttpStatus.OK);

    }
    private boolean checkForInsufficientFund(double currentBalance, double amountToBeWithdraw){
        double remainingAmount = currentBalance - amountToBeWithdraw;
        if (remainingAmount <500){
            return false;
        }
        return true;
    }
    public void otherWithdrawal(TransactionTypeDto transactionTypeDto, String accountNumber,double currentBalance,String transactionPurpose) throws DataNotFoundException {
        BankAccount bankAccount = bankAccountService.getAccountByAccountNumber(accountNumber);
        TransactionType transactionType = new TransactionType();
        transactionType.setAmount(transactionTypeDto.getAmount());
        transactionType.setBankAccount(bankAccount);
        transactionType.setCurrentBalance(currentBalance);
        transactionType.setTransactionType(transactionPurpose);
        transactionType.setTransactionDate(new Date());
        transactionType.setDescription(transactionTypeDto.getDescription());
        transactionType.setDepositorOrWithDrawalName(transactionTypeDto.getDepositorOrWithDrawalName());
        transactionTypeRepository.save(transactionType);
    }
    public ResponseEntity<TransactionResponse> withDrawWithCard(CardWithdrawalDto cardWithdrawalDto, String accountNumber) throws DataNotFoundException, NoSuchAlgorithmException, DataNotAcceptableException {
        if (checkForCardExpiration(accountNumber)){
            throw new DataNotAcceptableException("Your debit Card has expired, please get a new one");
        }
        BankAccount bankAccount = bankAccountService.getAccountByAccountNumber(accountNumber);
        DebitCard debitCard = debitCardService.getCardDetails(accountNumber);
        String cardNumber = debitCard.getCardNumber();
        LocalDate cardExpiringDate = debitCard.getExpiringDate();
        String secretPin = debitCard.getCardSecretPin();
        BankAccount debitCardAccountOwner = debitCard.getBankAccount();
        if (!cardWithdrawalDto.getCardNumber().equalsIgnoreCase(cardNumber)){
            throw new DataNotFoundException("Incorrect Card Number");
        }if (!cardWithdrawalDto.getCardExpiringDate().equalsIgnoreCase(cardExpiringDate.toString())){
            throw new DataNotFoundException("Incorrect Card Expiring Date");
        }if (!secretPin.equalsIgnoreCase(hashPassword(cardWithdrawalDto.getCardSecretPin()))){
            throw new DataNotFoundException("Incorrect Secret Pin");
        }if (cardWithdrawalDto.getAmount()<500){
            throw new DataNotAcceptableException("Amount to be withdrawn must not be less than 500");
        }
            if (!bankAccount.equals(debitCardAccountOwner)){
            throw new DataNotFoundException("This Card does not belong to this bank Account");
        }
        double currentBalance = bankAccount.getCurrentBalance();
        double withDrawalAmount = cardWithdrawalDto.getAmount();
        if (!checkForInsufficientFund(currentBalance,withDrawalAmount)){
            throw new DataNotAcceptableException("Insufficient fund");
        }
        double newBalance = currentBalance - withDrawalAmount;
        TransactionType transactionType = new TransactionType();
        transactionType.setDepositorOrWithDrawalName("CARD WITHDRAWAL");
        transactionType.setTransactionType("DEBIT");
        transactionType.setCurrentBalance(newBalance);
        transactionType.setBankAccount(bankAccount);
        transactionType.setAmount(cardWithdrawalDto.getAmount());
        transactionType.setDescription("ATM");
        transactionType.setTransactionDate(new Date());
        bankAccountService.updateAccountBalance(bankAccount,newBalance,accountNumber);
        transactionTypeRepository.save(transactionType);
        return new ResponseEntity<>(new TransactionResponse("Withdrawal","Successful","Your Account "+bankAccount.getAccountNumber()+
                " has been debited with "+cardWithdrawalDto.getAmount()+" at "+transactionType.getTransactionDate()+" by " +
                "ATM Card Withdrawal, your current balance is #"+newBalance), HttpStatus.OK);

    }
    public ResponseEntity<TransactionResponse> transferMoneyToAnotherAccount(TransferTransactionDto transferTransactionDto, String recipientAccountNumber) throws DataNotAcceptableException, DataNotFoundException, NoSuchAlgorithmException {
        if (checkForCardExpiration(transferTransactionDto.getAccountNumberFrom())){
            throw new DataNotAcceptableException("Your debit Card has expired, please get a new one");
        }
        BankAccount senderAccount = bankAccountService.getAccountByAccountNumber(transferTransactionDto.getAccountNumberFrom());
        BankAccount recipientAccount = bankAccountService.getAccountByAccountNumber(recipientAccountNumber);
        DebitCard debitCard = debitCardService.getCardDetails(transferTransactionDto.getAccountNumberFrom());
        String cardNumber = debitCard.getCardNumber();
        LocalDate cardExpiringDate = debitCard.getExpiringDate();
        String secretPin = debitCard.getCardSecretPin();
        BankAccount debitCardAccountOwner = debitCard.getBankAccount();
        if (transferTransactionDto.getAmount()<=0){
            throw new DataNotAcceptableException("Amount must be greater than zero");
        }
        if (!transferTransactionDto.getCardNumber().equalsIgnoreCase(cardNumber)){
            throw new DataNotFoundException("Incorrect Card Number");
        }
        if (!transferTransactionDto.getCardExpiringDate().equalsIgnoreCase(cardExpiringDate.toString())){
            throw new DataNotFoundException("Incorrect Card Expiring date");
        }
        if (!secretPin.equalsIgnoreCase(hashPassword(transferTransactionDto.getCardSecretPin()))){
            throw new DataNotFoundException("Incorrect Secret Pin");
        }
        if (!senderAccount.equals(debitCardAccountOwner)){
            throw new DataNotFoundException("You are not the owner of this card");
        }
        if (senderAccount.equals(recipientAccount)){
            throw new DataNotAcceptableException("You can't transfer money to the same account. The sender and recipient account Number is the same");
        }
        double senderAccountCurrentBalance = senderAccount.getCurrentBalance();
        double recipientAccountCurrentBalance = recipientAccount.getCurrentBalance();
        double transferAmount = transferTransactionDto.getAmount();
        if (!checkForInsufficientFund(senderAccountCurrentBalance,transferAmount)){
            throw new DataNotAcceptableException("Insufficient fund");
        }
        double senderAccountNewBalance = senderAccountCurrentBalance - transferAmount;
        double recipientAccountNewBalance = recipientAccountCurrentBalance + transferAmount;
        TransactionType senderTransferType = new TransactionType();
        TransactionType receiverTransferType = new TransactionType();
        senderTransferType.setDepositorOrWithDrawalName(recipientAccount.getCustomer().getName());
        receiverTransferType.setDepositorOrWithDrawalName(senderAccount.getCustomer().getName());
        senderTransferType.setTransactionType("DEBIT");
        receiverTransferType.setTransactionType("CREDIT");
        senderTransferType.setCurrentBalance(senderAccountNewBalance);
        receiverTransferType.setCurrentBalance(recipientAccountNewBalance);
        senderTransferType.setBankAccount(senderAccount);
        receiverTransferType.setBankAccount(recipientAccount);
        senderTransferType.setAmount(transferAmount);
        receiverTransferType.setAmount(transferAmount);
        senderTransferType.setDescription("Account to Account Transfer");
        receiverTransferType.setDescription("Account to Account Transfer");
        senderTransferType.setTransactionDate(new Date());
        receiverTransferType.setTransactionDate(new Date());

        bankAccountService.updateAccountBalance(senderAccount,senderAccountNewBalance,transferTransactionDto.getAccountNumberFrom());
        bankAccountService.updateAccountBalance(recipientAccount,recipientAccountNewBalance,recipientAccountNumber);
        transactionTypeRepository.save(senderTransferType);
        transactionTypeRepository.save(receiverTransferType);
        return new ResponseEntity<>(new TransactionResponse("Transfer","Successful","Your Account "+senderAccount.getAccountNumber()+
                " has been debited with "+transferTransactionDto.getAmount()+" at "+senderTransferType.getTransactionDate()+" by " +
                "Account to Account Transfer to "+recipientAccount.getAccountNumber()+". "+recipientAccount.getCustomer().getName()+", your current balance is #"+senderAccountNewBalance), HttpStatus.OK);



    }
    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(password.getBytes());
        byte[] digest = messageDigest.digest();
        String hash = DatatypeConverter.printHexBinary(digest).toLowerCase().substring(0,12);
        return hash;
    }
    public List<TransactionType> getAllTransaction(){
        List<TransactionType> transactionTypeList = transactionTypeRepository.findAll();
        return transactionTypeList;
    }
    public List<BankAccountStatement> getAccountStatement(BankAccountStatementDto bankAccountStatementDto){
        List<TransactionType> transactionTypeList = transactionTypeRepository.getTransactionStatement(bankAccountStatementDto.getStartDate(),bankAccountStatementDto.getEndDate());
        List<BankAccountStatement> bankAccountStatementList = new ArrayList<>();
        for (TransactionType transactionType: transactionTypeList){
            BankAccountStatement bankAccountStatement = new BankAccountStatement();
            bankAccountStatement.setAmount(transactionType.getAmount());
            bankAccountStatement.setCurrentBalance(transactionType.getCurrentBalance());
            bankAccountStatement.setDepositorOrWithDrawalName(transactionType.getDepositorOrWithDrawalName());
            bankAccountStatement.setDescription(transactionType.getDescription());
            bankAccountStatement.setTransactionDate(transactionType.getTransactionDate().toString());
            bankAccountStatement.setTransactionType(transactionType.getTransactionType());
            bankAccountStatement.setId(transactionType.getId());
            bankAccountStatementList.add(bankAccountStatement);
        }
        return bankAccountStatementList;
    }
    public boolean checkForCardExpiration(String accountNumber) throws DataNotFoundException {
        boolean expired = false;
        DebitCard debitCard = debitCardService.getCardDetails(accountNumber);
        LocalDate localDate = debitCard.getExpiringDate();
        LocalDate currentDate = LocalDate.now();
        if (currentDate.isAfter(localDate)){
            expired = true;
        }else {
            expired = false;
        }
        return expired;

    }
    public List<BankAccountStatement> getAccountStatementForTheUser(String accountNumber) throws DataNotFoundException {
        BankAccount bankAccount = bankAccountService.getAccountByAccountNumber(accountNumber);
        List<TransactionType> transactionTypeList = transactionTypeRepository.findByBankAccount(bankAccount);
        List<BankAccountStatement> bankAccountStatementList = new ArrayList<>();
        for (TransactionType transactionType: transactionTypeList){
            BankAccountStatement bankAccountStatement = new BankAccountStatement();
            bankAccountStatement.setId(transactionType.getId());
            bankAccountStatement.setTransactionType(transactionType.getTransactionType());
            bankAccountStatement.setTransactionDate(transactionType.getTransactionDate().toString());
            bankAccountStatement.setDescription(transactionType.getDescription());
            bankAccountStatement.setDepositorOrWithDrawalName(transactionType.getDepositorOrWithDrawalName());
            bankAccountStatement.setCurrentBalance(transactionType.getCurrentBalance());
            bankAccountStatement.setAmount(transactionType.getAmount());

            bankAccountStatementList.add(bankAccountStatement);

        }
        return bankAccountStatementList;
    }
    public List<BankAccountStatement> getUserAccountStatement(BankAccountStatementDto bankAccountStatementDto, String accountNumber) throws DataNotFoundException {
        BankAccount bankAccount = bankAccountService.getAccountByAccountNumber(accountNumber);
        List<TransactionType> transactionTypeList = transactionTypeRepository.findByBankAccountAndTransactionDate(bankAccount,bankAccountStatementDto.getStartDate(),bankAccountStatementDto.getEndDate());
        //the TransactionType has so many data and I don't need all the data in it
        //I only want to retrieve the important information and store on this BankAccountStatement
        List<BankAccountStatement> bankAccountStatementList = new ArrayList<>();
        for (TransactionType transactionType: transactionTypeList){
            BankAccountStatement bankAccountStatement = new BankAccountStatement();
            bankAccountStatement.setId(transactionType.getId());
            bankAccountStatement.setTransactionType(transactionType.getTransactionType());
            bankAccountStatement.setTransactionDate(transactionType.getTransactionDate().toString());
            bankAccountStatement.setDescription(transactionType.getDescription());
            bankAccountStatement.setDepositorOrWithDrawalName(transactionType.getDepositorOrWithDrawalName());
            bankAccountStatement.setCurrentBalance(transactionType.getCurrentBalance());
            bankAccountStatement.setAmount(transactionType.getAmount());

            bankAccountStatementList.add(bankAccountStatement);
        }
        return bankAccountStatementList;
    }
}
