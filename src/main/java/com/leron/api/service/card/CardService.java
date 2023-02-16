package com.leron.api.service.card;

import com.leron.api.mapper.card.CardMapper;
import com.leron.api.model.DTO.card.CardDTO;
import com.leron.api.model.DTO.card.CardRequest;
import com.leron.api.model.DTO.card.CardResponse;
import com.leron.api.model.entities.BankEntity;
import com.leron.api.model.entities.CardEntity;
import com.leron.api.model.entities.UserEntity;
import com.leron.api.repository.BankRepository;
import com.leron.api.repository.CardRepository;
import com.leron.api.repository.UserRepository;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataRequest;
import com.leron.api.responses.DataResponse;
import com.leron.api.validator.card.CardValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final BankRepository bankRepository;

    public CardService(CardRepository cardRepository, UserRepository userRepository, BankRepository bankRepository) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
        this.bankRepository = bankRepository;
    }

    public DataListResponse<CardDTO> list(){
        List<CardEntity> cardEntities = cardRepository.findAll();
        List<BankEntity> bankEntityList = bankRepository.findAll();
        List<UserEntity> userEntityList =  userRepository.findAll();

        DataListResponse<CardDTO> cardDTO  =CardMapper.cardEntitiesToDataListResponse(cardEntities, bankEntityList, userEntityList);

        return cardDTO;
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
