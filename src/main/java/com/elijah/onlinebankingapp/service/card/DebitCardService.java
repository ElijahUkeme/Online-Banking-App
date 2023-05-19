package com.elijah.onlinebankingapp.service.card;

import com.elijah.onlinebankingapp.dto.card.DebitCardDto;
import com.elijah.onlinebankingapp.dto.transaction.TransactionTypeDto;
import com.elijah.onlinebankingapp.exception.DataNotAcceptableException;
import com.elijah.onlinebankingapp.exception.DataNotFoundException;
import com.elijah.onlinebankingapp.model.account.BankAccount;
import com.elijah.onlinebankingapp.model.card.CardType;
import com.elijah.onlinebankingapp.model.card.DebitCard;
import com.elijah.onlinebankingapp.repository.card.DebitCardRepository;
import com.elijah.onlinebankingapp.response.customer.SignInResponse;
import com.elijah.onlinebankingapp.response.customer.SignUpResponse;
import com.elijah.onlinebankingapp.service.bank.BankAccountService;
import com.elijah.onlinebankingapp.service.transaction.TransactionTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class DebitCardService {

    @Autowired
    private DebitCardRepository debitCardRepository;
    @Autowired
    private BankAccountService bankAccountService;
    @Autowired
    private CardTypeService cardTypeService;
    @Autowired
    private TransactionTypeService transactionTypeService;

    //create a debit card to a certain customer with so many validation
    public ResponseEntity<SignUpResponse> createDebitCard(DebitCardDto debitCardDto, String accountNumber) throws DataNotFoundException, DataNotAcceptableException {
        BankAccount bankAccount = bankAccountService.getAccountByAccountNumber(accountNumber);
        double currentBalance = bankAccount.getCurrentBalance();
        DebitCard card = debitCardRepository.findByBankAccount(bankAccount);
        if (Objects.nonNull(card)){
            //which means this user has already have a debit card
            //proceed to check if the card has expired
            LocalDate localDate = card.getExpiringDate();
            LocalDate currentDate = LocalDate.now();
            if (currentDate.isEqual(localDate)|| currentDate.isBefore(localDate)){
                log.info("Your card expiring date is "+localDate);
                log.info("Your current date is "+currentDate);
                throw new DataNotAcceptableException("You can't create another card yet, your old card has not expired");
            }
        }
        //go ahead and generate a debit card to the user and deduct the card charges from his/her account
        String transactionPurpose = "Debit Card Charge";
        double cardCharge = 0;
        boolean found = false;
        List<CardType> cardTypeList = cardTypeService.getAllAvailableCardType();
        for (CardType cardType: cardTypeList) {
            if (debitCardDto.getCardType().equalsIgnoreCase(cardType.getCardTypeName())){
                found = true;
                break;
            }
        }
        if (!found){
            throw new DataNotAcceptableException("There is no card with such name");
        }
        if (debitCardDto.getCardType().equalsIgnoreCase("Visa")){
            cardCharge = 1100;
        }if (debitCardDto.getCardType().equalsIgnoreCase("Verve")){
            cardCharge = 1000;
        }if (debitCardDto.getCardType().equalsIgnoreCase("Master")){
            cardCharge = 1500;
        }
        double newBalance = currentBalance - cardCharge;
        if (newBalance <500){
            throw new DataNotAcceptableException("You don't have enough amount in your account to purchase a debit card, please credit your account and try again");
        }
        if (debitCardDto.getCardSecretPin().length()!=4){
            throw new DataNotAcceptableException("Your pin number must not be less than or greater than four digits");
        }
        String encryptedCustomerPassword = debitCardDto.getCardSecretPin();
        try {
            encryptedCustomerPassword = hashPassword(debitCardDto.getCardSecretPin());
        }catch (Exception e) {
            e.printStackTrace();
        }
        TransactionTypeDto transactionTypeDto = new TransactionTypeDto();
        transactionTypeDto.setAmount(cardCharge);
        transactionTypeDto.setDescription("ATM Card Charge from the Bank");
        transactionTypeDto.setDepositorOrWithDrawalName("Bank");
        DebitCard debitCard = new DebitCard();
        debitCard.setBankAccount(bankAccount);
        debitCard.setCardNumber(debitCardDto.getCardNumber());
        debitCard.setCardSecretPin(encryptedCustomerPassword);
        debitCard.setCardType(debitCardDto.getCardType());
        debitCard.setExpiringDate(debitCardDto.getExpiringDate());
        transactionTypeService.otherWithdrawal(transactionTypeDto,accountNumber,newBalance,transactionPurpose);
        bankAccountService.updateAccountBalance(bankAccount,newBalance,accountNumber);
        debitCardRepository.save(debitCard);
        return new ResponseEntity<>(new SignUpResponse(true,"You have successfully gotten a debit card. #"+cardCharge+" has been deducted from your account "+accountNumber+" for the card charge"), HttpStatus.CREATED);
    }
    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(password.getBytes());
        byte[] digest = messageDigest.digest();
        String hash = DatatypeConverter.printHexBinary(digest).toLowerCase().substring(0,12);
        return hash;
    }

    //return the details of the customer's card based on the entered account number
    public DebitCard getCardDetails(String accountNumber) throws DataNotFoundException {
        BankAccount bankAccount = bankAccountService.getAccountByAccountNumber(accountNumber);
        DebitCard debitCard = debitCardRepository.findByBankAccount(bankAccount);
        if (Objects.isNull(debitCard)){
            throw new DataNotFoundException("There is no Card for this Customer");
        }
        return debitCard;
    }
}
