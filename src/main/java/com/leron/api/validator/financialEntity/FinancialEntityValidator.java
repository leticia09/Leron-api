package com.leron.api.validator.financialEntity;

import com.leron.api.model.DTO.financialEntity.FinancialEntityRequest;
import com.leron.api.model.DTO.financialEntity.FinancialEntityResponse;
import com.leron.api.model.entities.FinancialEntity;
import com.leron.api.responses.ApplicationBusinessException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class FinancialEntityValidator {
    public static void validate(FinancialEntityRequest request, List<FinancialEntity> currentEntrance) throws ApplicationBusinessException {

        AtomicReference<Boolean> moneyExists = new AtomicReference<>(false);
        AtomicReference<Boolean> invalidDate = new AtomicReference<>(false);

//        request.forEach(res -> {
//            currentEntrance.forEach(res1 -> {
//                if (res.getCurrency().equalsIgnoreCase(res1.getCurrency()) && res.getOwnerId().equals(res1.getOwnerId())) {
//                    moneyExists.set(Boolean.TRUE);
//                }
//            });
//        });

//        List<MoneyRequest> moneyRequests = request.stream().filter(re -> re.getCurrency().equalsIgnoreCase("R$")).collect(Collectors.toList());
//        List<MoneyRequest> moneyRequests1 = request.stream().filter(re -> re.getCurrency().equalsIgnoreCase("US$")).collect(Collectors.toList());
//        List<MoneyRequest> moneyRequests2 = request.stream().filter(re -> re.getCurrency().equalsIgnoreCase("€")).collect(Collectors.toList());

        if (invalidDate.get()) {
            throw new ApplicationBusinessException("ERROR", "INVALID_EXPIRATION_DATE");
        }


//        if (moneyExists.get() || moneyRequests.size() > 1 || moneyRequests1.size() > 1 || moneyRequests2.size() > 1) {
//            throw new ApplicationBusinessException("ERROR", "MONEY_ALREADY_EXISTS");
//        }
    }

    public static void validateEdit(List<FinancialEntity> current, FinancialEntityResponse money) throws ApplicationBusinessException {
        AtomicReference<Boolean> moneyExists = new AtomicReference<>(false);

//        current.forEach(res -> {
//            if (res.getCurrency().equalsIgnoreCase(money.getCurrency()) && res.getOwnerId().equals(money.getOwnerId())  && !res.getId().equals(money.getId())) {
//                moneyExists.set(Boolean.TRUE);
//            }
//        });

        if (moneyExists.get()) {
            throw new ApplicationBusinessException("ERROR", "MONEY_ALREADY_EXISTS");
        }

    }
}
