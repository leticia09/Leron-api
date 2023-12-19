package com.leron.api.mapper.financialEntity;

import com.leron.api.model.DTO.financialEntity.CardFinancialEntityRequest;
import com.leron.api.model.DTO.financialEntity.CardFinancialEntityResponse;
import com.leron.api.model.DTO.financialEntity.FinancialEntityRequest;
import com.leron.api.model.DTO.financialEntity.FinancialEntityResponse;
import com.leron.api.model.entities.CardFinancialEntity;
import com.leron.api.model.entities.FinancialEntity;
import com.leron.api.responses.DataListResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class FinancialEntityMapper {
    public static FinancialEntity requestToEntity(FinancialEntityRequest request) {
       FinancialEntity response = new FinancialEntity();

        response.setName(request.getName());
        response.setCardFinancialEntityList(getCards(request.getCardFinancialEntityRequestList(), request.getUserAuthId()));
        response.setCreatedIn(new Date());
        response.setDeleted(false);
        response.setUserAuthId(request.getUserAuthId());

        return response;
    }

    public static DataListResponse<FinancialEntityResponse> entityToResponse(List<FinancialEntity> entity) {
        DataListResponse<FinancialEntityResponse> response = new DataListResponse<>();
        List<FinancialEntityResponse> financialEntityResponses = new ArrayList<>();

        entity.forEach(request -> {
            FinancialEntityResponse financialEntityResponse = new FinancialEntityResponse();
            financialEntityResponse.setId(request.getId());
            financialEntityResponse.setName(request.getName());
            financialEntityResponse.setCardFinancialEntityResponseList(getCardsResponse(request.getCardFinancialEntityList()));
            financialEntityResponses.add(financialEntityResponse);
        });

        response.setData(financialEntityResponses);
        return response;
    }

    private static List<CardFinancialEntity> getCards(List<CardFinancialEntityRequest> requests, Long authId) {
        List<CardFinancialEntity> response = new ArrayList<>();

        requests.forEach(res -> {
            CardFinancialEntity cardFinancialEntity = new CardFinancialEntity();
            cardFinancialEntity.setCardName(res.getCardName());
            cardFinancialEntity.setBalance(new BigDecimal(res.getBalance().replace(",", ".")));
            cardFinancialEntity.setFinalCard(res.getFinalCard());
            cardFinancialEntity.setCurrency(res.getCurrency());
            cardFinancialEntity.setModality(res.getModality());
            cardFinancialEntity.setOwnerId(res.getOwnerId());


            cardFinancialEntity.setCreatedIn(new Date());
            cardFinancialEntity.setDeleted(false);
            cardFinancialEntity.setUserAuthId(authId);
            response.add(cardFinancialEntity);
        });
        return response;
    }

    private static List<CardFinancialEntityResponse> getCardsResponse(List<CardFinancialEntity> requests) {
        List<CardFinancialEntityResponse> response = new ArrayList<>();

        requests.forEach(res -> {
            CardFinancialEntityResponse cardFinancialEntity = new CardFinancialEntityResponse();
            cardFinancialEntity.setId(res.getId());
            cardFinancialEntity.setCardName(res.getCardName());
            cardFinancialEntity.setBalance(res.getBalance());
            cardFinancialEntity.setFinalCard(res.getFinalCard());
            cardFinancialEntity.setCurrency(res.getCurrency());
            cardFinancialEntity.setModality(res.getModality());
            cardFinancialEntity.setOwnerId(res.getOwnerId());
            response.add(cardFinancialEntity);
        });
        return response;
    }
}
