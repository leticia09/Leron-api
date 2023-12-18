package com.leron.api.service.money;

import com.leron.api.mapper.money.MoneyMapper;
import com.leron.api.model.DTO.money.MoneyRequest;
import com.leron.api.model.DTO.money.MoneyResponse;
import com.leron.api.model.entities.Money;
import com.leron.api.repository.MoneyRepository;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataResponse;
import com.leron.api.validator.money.ValidatorMoney;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MoneyService {
    final MoneyRepository moneyRepository;

    public MoneyService(MoneyRepository moneyRepository) {
        this.moneyRepository = moneyRepository;
    }

    public DataResponse<MoneyResponse>  create(List<MoneyRequest> request) throws ApplicationBusinessException {
        DataResponse<MoneyResponse> response = new DataResponse<>();

        List<Money> moneys = moneyRepository.findAllByUserAuthIdAndDeletedFalse(request.get(0).getUserAuthId());

        ValidatorMoney.validate(request, moneys);

        moneyRepository.saveAll(MoneyMapper.requestToEntity(request));

        response.setSeverity("success");
        response.setMessage("success");
        return response;
    }

    public DataListResponse<MoneyResponse> list(Long userId) {
        List<Money> moneyList = moneyRepository.findAllByUserAuthIdAndDeletedFalse(userId);
        return MoneyMapper.entityToResponse(moneyList);
    }
}
