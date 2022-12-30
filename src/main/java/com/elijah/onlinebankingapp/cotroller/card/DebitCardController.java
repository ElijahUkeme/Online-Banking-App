package com.elijah.onlinebankingapp.cotroller.card;

import com.elijah.onlinebankingapp.dto.card.DebitCardDto;
import com.elijah.onlinebankingapp.exception.DataNotAcceptableException;
import com.elijah.onlinebankingapp.exception.DataNotFoundException;
import com.elijah.onlinebankingapp.model.card.DebitCard;
import com.elijah.onlinebankingapp.response.customer.SignUpResponse;
import com.elijah.onlinebankingapp.service.card.DebitCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DebitCardController {

    @Autowired
    private DebitCardService debitCardService;

    @PostMapping("/debitCard/create")
    public ResponseEntity<SignUpResponse> createDebitCard(@RequestBody DebitCardDto debitCardDto, @RequestParam("accountNumber") String accountNumber) throws DataNotAcceptableException, DataNotFoundException {
        return debitCardService.createDebitCard(debitCardDto,accountNumber);
    }
    @PostMapping("/debitCard/info")
    public ResponseEntity<DebitCard> getCardDetailsForCustomer(@RequestParam("accountNumber") String accountNumber) throws DataNotFoundException {

        return new ResponseEntity<>(debitCardService.getCardDetails(accountNumber),HttpStatus.OK);
    }
}
