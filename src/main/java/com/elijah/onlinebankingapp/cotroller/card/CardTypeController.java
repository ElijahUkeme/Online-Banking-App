package com.elijah.onlinebankingapp.cotroller.card;

import com.elijah.onlinebankingapp.dto.card.CardTypeDto;
import com.elijah.onlinebankingapp.exception.DataAlreadyExistException;
import com.elijah.onlinebankingapp.exception.DataNotFoundException;
import com.elijah.onlinebankingapp.model.card.CardType;
import com.elijah.onlinebankingapp.response.customer.SignUpResponse;
import com.elijah.onlinebankingapp.service.card.CardTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CardTypeController {

    @Autowired
    private CardTypeService cardTypeService;

    @PostMapping("/card/type/create")
    public ResponseEntity<SignUpResponse> createCardType(@RequestBody CardTypeDto cardTypeDto) throws DataAlreadyExistException {
        return cardTypeService.createCardType(cardTypeDto);
    }
    @GetMapping("/card/getAll")
    public ResponseEntity<List<CardType>> allCardTypes(){
         return new ResponseEntity<>(cardTypeService.getAllAvailableCardType(),HttpStatus.OK);
    }
    @PostMapping("/card/type/find")
    public ResponseEntity<SignUpResponse> getAllCard(@RequestParam("cardType") String cardType) throws DataNotFoundException {
        cardTypeService.getCards(cardType);
        return new ResponseEntity<>(new SignUpResponse(true,"card found"),HttpStatus.OK);
    }
}
