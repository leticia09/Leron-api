package com.leron.api.mapper.money;

import com.leron.api.model.DTO.entrance.EntranceRequest;
import com.leron.api.model.DTO.money.MoneyRequest;
import com.leron.api.model.DTO.money.MoneyResponse;
import com.leron.api.model.entities.Entrance;
import com.leron.api.model.entities.Money;
import com.leron.api.repository.MoneyRepository;
import com.leron.api.responses.DataListResponse;
import com.leron.api.utils.FormatDate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
public class MoneyMapper {
    public static List<Money> requestToEntity(List<MoneyRequest> requests) {
        List<Money> response = new ArrayList<>();

        requests.forEach(request -> {
            Money money = new Money();
            money.setOwnerId(request.getOwnerId());
            money.setCurrency(request.getCurrency());
            money.setValue(request.getValue());
            money.setCreatedIn(new Date());
            money.setDeleted(false);
            money.setUserAuthId(request.getUserAuthId());
            response.add(money);
        });

        return response;
    }

    public static DataListResponse<MoneyResponse> entityToResponse(List<Money> entity) {
        DataListResponse<MoneyResponse> response = new DataListResponse<>();
        List<MoneyResponse> moneyResponses = new ArrayList<>();

        entity.forEach(request -> {
            MoneyResponse money = new MoneyResponse();
            money.setOwnerId(request.getOwnerId());
            money.setCurrency(request.getCurrency());
            money.setValue(request.getValue());
            moneyResponses.add(money);
        });

        response.setData(moneyResponses);
        return response;
    }
}
