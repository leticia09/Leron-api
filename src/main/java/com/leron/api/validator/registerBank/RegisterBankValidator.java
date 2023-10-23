package com.leron.api.validator.registerBank;

import com.leron.api.model.DTO.registerBank.RegisterBankRequest;
import com.leron.api.model.entities.Bank;
import com.leron.api.responses.ApplicationBusinessException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class RegisterBankValidator {

    public static void validate(RegisterBankRequest request, List<Bank> currentBankList) throws ApplicationBusinessException {

        if (request.getName() == null || request.getName().isEmpty() ||
                request.getAccounts() == null || request.getAccounts().isEmpty()) {
            throw new ApplicationBusinessException("ERROR", "REQUIRED_FIELDS_MISSING");
        }

        AtomicReference<Boolean> isSameBankName = new AtomicReference<>(false);
        AtomicReference<Boolean> areAccountNumbersUnique = new AtomicReference<>(false);
        AtomicReference<Boolean> areCardNumbersUnique = new AtomicReference<>(false);


        currentBankList.forEach(bank -> {
            if (bank.getName().equalsIgnoreCase(request.getName())) {
                isSameBankName.set(true);
            }
            bank.getAccounts().forEach(account -> {
                if (request.getAccounts().stream()
                        .anyMatch(reqAccount -> reqAccount.getAccountNumber().equals(account.getAccountNumber()))) {
                    areAccountNumbersUnique.set(true);
                }

                account.getCards().forEach(card -> {
                    request.getAccounts().forEach(reqAccount -> {
                        if (reqAccount.getCards().stream()
                                .anyMatch(reqCard -> reqCard.getFinalNumber().equals(card.getFinalNumber()))) {
                            areCardNumbersUnique.set(true);
                        }
                    });
                });
            });
        });

        if (isSameBankName.get()) {
            throw new ApplicationBusinessException("ERROR", "BANK_NAME_ALREADY_EXISTS");
        }

        if (areAccountNumbersUnique.get()) {
            throw new ApplicationBusinessException("ERROR", "DUPLICATE_ACCOUNT_NUMBER");
        }

        if (areCardNumbersUnique.get()) {
            throw new ApplicationBusinessException("ERROR", "DUPLICATE_CARD_NUMBER");
        }

    }
}
