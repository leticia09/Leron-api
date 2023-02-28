package com.leron.api.validator.card;

import com.leron.api.model.DTO.card.CardRequest;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataRequest;
import org.springframework.stereotype.Component;

import java.net.CacheRequest;

@Component
public class CardValidator {
    public static void validatorCard(DataRequest<CardRequest> cardRequest) throws ApplicationBusinessException {

        if(cardRequest.getData().getFinalCard() == null){
            throw new ApplicationBusinessException("Lascou", "FINAL");
        }
        if(cardRequest.getData().getStatus() == null){
            throw new ApplicationBusinessException("Lascou", "STATUS");
        }
        if(cardRequest.getData().getDueDate() == null){
            throw new ApplicationBusinessException("Lascou", "VENCIMENTO");
        }
        if(cardRequest.getData().getBankId() == null){
            throw new ApplicationBusinessException("Lascou", "BANCO");
        }
        if(cardRequest.getData().getUserId() == null){
            throw new ApplicationBusinessException("Lascou", "USER");
        }
        if(cardRequest.getData().getModality() == null){
            throw new ApplicationBusinessException("Lascou", "MODALIDADE");
        }
    }
}
