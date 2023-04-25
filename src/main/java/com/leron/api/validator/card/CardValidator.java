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
            throw new ApplicationBusinessException("Lascou", "FINAL_IS_EMPTY");
        }
        if(cardRequest.getData().getStatus() == null){
            throw new ApplicationBusinessException("Lascou", "STATUS_IS_EMPTY");
        }
        if(cardRequest.getData().getDueDate() == null){
            throw new ApplicationBusinessException("Lascou", "DUE_DATE_IS_EMPTY");
        }
        if(cardRequest.getData().getBankId() == null){
            throw new ApplicationBusinessException("Lascou", "BANK_IS_EMPTY");
        }
        if(cardRequest.getData().getUserId() == null){
            throw new ApplicationBusinessException("Lascou", "USER_IS_EMPTY");
        }
        if(cardRequest.getData().getModality() == null){
            throw new ApplicationBusinessException("Lascou", "MODALITY_IS_EMPTY");
        }
    }
}
