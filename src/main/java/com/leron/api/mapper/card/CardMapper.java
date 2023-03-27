package com.leron.api.mapper.card;

import com.leron.api.model.DTO.card.CardRequest;
import com.leron.api.model.DTO.card.CardResponse;
import com.leron.api.model.entities.BankEntity;
import com.leron.api.model.entities.CardEntity;
import com.leron.api.model.entities.UserEntity;
import com.leron.api.responses.DataListResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class CardMapper {
    public static DataListResponse<CardResponse> cardEntitiesToDataListResponse(List<CardEntity> cardEntities, List<BankEntity> bankEntityList, List<UserEntity> userEntityList){

        DataListResponse<CardResponse> response = new DataListResponse<>();
        List<CardResponse> responseList = new ArrayList<>();

        for (CardEntity card : cardEntities) {
            CardResponse cardDTO = new CardResponse();
            bankEntityList.forEach(bank -> {
                if(bank.getId().equals(card.getBankId())){
                    cardDTO.setBankName(bank.getName());
                }
            });
            userEntityList.forEach(user -> {
                if(user.getId().equals(card.getUserId())){
                    cardDTO.setUserName(user.getName());
                }
            });
            cardDTO.setId(card.getId());
            cardDTO.setStatus(card.getStatus());
            cardDTO.setFinalCard(card.getFinalCard());
            cardDTO.setBankId(card.getBankId());
            cardDTO.setDueDate(card.getDueDate());
            cardDTO.setBillClose(card.getBillClose());
            cardDTO.setModality(card.getModality());
            cardDTO.setUserId(card.getUserId());
            cardDTO.setNickName(card.getNickName());
            cardDTO.setUserAuthId(card.getUserAuthId());

            responseList.add(cardDTO);
        }
        response.setData(responseList);

        return response;
    }

    public static CardEntity createCardFromCardRequest(CardRequest cardRequest) {

        CardEntity card = new CardEntity();

        card.setStatus(cardRequest.getStatus());
        card.setFinalCard(Long.valueOf(cardRequest.getFinalCard()));
        card.setBankId(Long.valueOf(cardRequest.getBankId()));
        card.setDueDate(Long.valueOf(cardRequest.getDueDate()));
        card.setBillClose(cardRequest.getBillClose());
        card.setModality(cardRequest.getModality());
        card.setUserId(Long.valueOf(cardRequest.getUserId()));
        card.setCreatedIn(new Date());
        card.setNickName(cardRequest.getNickName());
        card.setUserAuthId(cardRequest.getUserAuthId());

        return card;
    }

    public static CardResponse createCardResponse  (CardEntity card) {

        CardResponse cardResponse = new CardResponse();

        cardResponse.setStatus(card.getStatus());
        cardResponse.setFinalCard(card.getFinalCard());
        cardResponse.setBankId(card.getBankId());
        cardResponse.setBillClose(card.getBillClose());
        cardResponse.setDueDate(card.getDueDate());
        cardResponse.setModality(card.getModality());
        cardResponse.setUserId(card.getUserId());
        cardResponse.setId(card.getId());
        cardResponse.setNickName(card.getNickName());
        cardResponse.setUserAuthId(card.getUserAuthId());

        return cardResponse;
    }
}
