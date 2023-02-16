package com.leron.api.mapper.card;

import com.leron.api.model.DTO.card.CardDTO;
import com.leron.api.model.DTO.card.CardRequest;
import com.leron.api.model.DTO.card.CardResponse;
import com.leron.api.model.entities.CardEntity;
import com.leron.api.responses.DataListResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CardMapper {
    public static DataListResponse<CardDTO> cardEntitiesToDataListResponse(List<CardEntity> cardEntities){

        DataListResponse<CardDTO> response = new DataListResponse<>();
        List<CardDTO> responseList = new ArrayList<>();

        for (CardEntity card : cardEntities) {
            CardDTO cardDTO = new CardDTO();

            cardDTO.setId(card.getId());
            cardDTO.setStatus(card.getStatus());
            cardDTO.setFinalCard(card.getFinalCard());
            cardDTO.setBankId(card.getBankId());
            cardDTO.setDueDate(card.getDueDate());
            cardDTO.setModality(card.getModality());
            cardDTO.setUserId(card.getUserId());

            responseList.add(cardDTO);
        }
        response.setData(responseList);

        return response;
    }

    public static CardEntity createCardFromCardRequest(CardRequest cardRequest) {

        CardEntity card = new CardEntity();

        card.setStatus(cardRequest.getStatus());
        card.setFinalCard(cardRequest.getFinalCard());
        card.setBankId(cardRequest.getBankId());
        card.setDueDate(cardRequest.getDueDate());
        card.setModality(cardRequest.getModality());
        card.setUserId(cardRequest.getUserId());

        return card;
    }

    public static CardResponse createCardResponse  (CardEntity card) {

        CardResponse cardResponse = new CardResponse();

        cardResponse.setStatus(card.getStatus());
        cardResponse.setFinalCard(card.getFinalCard());
        cardResponse.setBankId(card.getBankId());
        cardResponse.setDueDate(card.getDueDate());
        cardResponse.setModality(card.getModality());
        cardResponse.setUserId(card.getUserId());
        cardResponse.setId(card.getId());

        return cardResponse;
    }
}
