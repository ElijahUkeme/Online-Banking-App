package com.elijah.onlinebankingapp.service.card;

import com.elijah.onlinebankingapp.dto.card.CardTypeDto;
import com.elijah.onlinebankingapp.exception.DataAlreadyExistException;
import com.elijah.onlinebankingapp.exception.DataNotFoundException;
import com.elijah.onlinebankingapp.model.card.CardType;
import com.elijah.onlinebankingapp.repository.card.CardTypeRepository;
import com.elijah.onlinebankingapp.response.customer.SignInResponse;
import com.elijah.onlinebankingapp.response.customer.SignUpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CardTypeService {
    @Autowired
    private CardTypeRepository cardTypeRepository;

    //create a debit card type to be used by customers such as VISA, VERVE and MASTER
    //will first loop through the available card type in the database to check if the
    //card type has already being created by the bank to avoid duplicate creation of card type
    public ResponseEntity<SignUpResponse> createCardType(CardTypeDto cardTypeDto) throws DataAlreadyExistException {
        List<CardType> cardTypeList = getAllAvailableCardType();
        for (CardType cardType: cardTypeList){
            String cardTypeName = cardType.getCardTypeName();
            if (cardTypeDto.getCardTypeName().equalsIgnoreCase(cardTypeName)){
                throw new DataAlreadyExistException("You have already added this card type into the database");
            }
        }
        CardType cardType = new CardType();
        cardType.setCardTypeName(cardTypeDto.getCardTypeName());
        cardTypeRepository.save(cardType);
        return new ResponseEntity<>(new SignUpResponse(true,"Card Type Created Successfully"), HttpStatus.CREATED);
    }

    //get all the available card type from the database
    public List<CardType> getAllAvailableCardType(){
        List<CardType> cardTypeList = cardTypeRepository.findAll();
        //this method returns a list of all the cardType from the db
        //and it's working perfectly
        return cardTypeList;
    }
    //loop to get see if a certain card type in available
    public void getCards(String cardType) throws DataNotFoundException {
        String cardName = "";
        List<CardType> cardTypeList = getAllAvailableCardType();
        boolean found = false;
        for (CardType cardTypes: cardTypeList){
            //loop through the list
            if (cardType.equalsIgnoreCase(cardTypes.getCardTypeName())){
                cardName = cardTypes.getCardTypeName();
                log.info("Congrats, there is card with the entered name "+cardName);
                found = true;
                break;
            }

        }
        if (!found){
            throw new DataNotFoundException("There is no card with such name");
        }

    }
}
