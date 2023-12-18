package com.leron.api.validator.money;

import com.leron.api.model.DTO.money.MoneyRequest;
import com.leron.api.model.entities.Money;
import com.leron.api.responses.ApplicationBusinessException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class ValidatorMoney {
    public static void validate(List<MoneyRequest> request, List<Money> currentEntrance) throws ApplicationBusinessException {

        AtomicReference<Boolean> moneyExists = new AtomicReference<>(false);
        AtomicReference<Boolean> invalidDate = new AtomicReference<>(false);

        request.forEach(res -> {
            currentEntrance.forEach(res1 -> {
                if(res.getCurrency().equalsIgnoreCase(res1.getCurrency()) && res.getOwnerId().equals(res1.getOwnerId())) {
                    moneyExists.set(Boolean.TRUE);
                }
            });
        });

        if (invalidDate.get()) {
            throw new ApplicationBusinessException("ERROR", "INVALID_EXPIRATION_DATE");
        }


        if (moneyExists.get()) {
            throw new ApplicationBusinessException("ERROR", "MONEY_ALREADY_EXISTS");
        }
    }
}
