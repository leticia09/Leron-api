package com.leron.api.service.card;

import com.leron.api.mapper.bank.BankMapper;
import com.leron.api.mapper.card.CardMapper;
import com.leron.api.model.DTO.bank.BankResponse;
import com.leron.api.model.DTO.card.CardDTO;
import com.leron.api.model.DTO.card.CardRequest;
import com.leron.api.model.DTO.card.CardResponse;
import com.leron.api.model.entities.CardEntity;
import com.leron.api.repository.CardRepository;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataRequest;
import com.leron.api.responses.DataResponse;
import com.leron.api.validator.card.CardValidator;
import org.springframework.stereotype.Service;

@Service
public class CardService {

    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public DataListResponse<CardDTO> list(){
        return CardMapper.cardEntitiesToDataListResponse(cardRepository.findAll());
    }

    public DataResponse<CardResponse> create(DataRequest<CardRequest> cardRequest) throws ApplicationBusinessException {
        DataResponse<CardResponse> response = new DataResponse<>();
        CardValidator.validatorCard(cardRequest);

        CardEntity card = CardMapper.createCardFromCardRequest(cardRequest.getData());
        cardRepository.save(card);
        CardResponse cardResponse = CardMapper.createCardResponse(card);
        response.setData(cardResponse);
        response.setMessage("Sucesso");
        return response;
    }
}
