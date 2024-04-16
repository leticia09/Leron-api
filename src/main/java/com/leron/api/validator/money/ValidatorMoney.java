package com.leron.api.validator.money;

import com.leron.api.model.DTO.money.MoneyRequest;
import com.leron.api.model.DTO.money.MoneyResponse;
import com.leron.api.model.entities.Money;
import com.leron.api.responses.ApplicationBusinessException;
import org.springframework.stereotype.Component;

import java.awt.event.MouseEvent;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Component
public class ValidatorMoney {
    public static void validate(List<MoneyRequest> request, List<Money> currentEntrance) throws ApplicationBusinessException {

        AtomicReference<Boolean> moneyExists = new AtomicReference<>(false);
        AtomicReference<Boolean> invalidDate = new AtomicReference<>(false);

        request.forEach(res -> {
            currentEntrance.forEach(res1 -> {
                if (res.getCurrency().equalsIgnoreCase(res1.getCurrency()) && res.getOwnerId().equals(res1.getOwnerId())) {
                    moneyExists.set(Boolean.TRUE);
                }
            });
        });

        List<MoneyRequest> moneyRequests = request.stream().filter(re -> re.getCurrency().equalsIgnoreCase("R$")).collect(Collectors.toList());
        List<MoneyRequest> moneyRequests1 = request.stream().filter(re -> re.getCurrency().equalsIgnoreCase("US$")).collect(Collectors.toList());
        List<MoneyRequest> moneyRequests2 = request.stream().filter(re -> re.getCurrency().equalsIgnoreCase("€")).collect(Collectors.toList());

        if (invalidDate.get()) {
            throw new ApplicationBusinessException("ERROR", "INVALID_EXPIRATION_DATE");
        }


        if (moneyExists.get() || moneyRequests.size() > 1 || moneyRequests1.size() > 1 || moneyRequests2.size() > 1) {
            throw new ApplicationBusinessException("ERROR", "MONEY_ALREADY_EXISTS");
        }
    }

    public static void validateEdit(List<Money> current, MoneyResponse money) throws ApplicationBusinessException {
        AtomicReference<Boolean> moneyExists = new AtomicReference<>(false);

        current.forEach(res -> {
            if (res.getCurrency().equalsIgnoreCase(money.getCurrency()) && res.getOwnerId().equals(money.getOwnerId())  && !res.getId().equals(money.getId())) {
                moneyExists.set(Boolean.TRUE);
            }
        });

        if (moneyExists.get()) {
            throw new ApplicationBusinessException("ERROR", "MONEY_ALREADY_EXISTS");
        }

    }
}
