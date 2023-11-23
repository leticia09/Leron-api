package com.leron.api.validator.registerBank;

import com.leron.api.model.DTO.registerBank.AccountResponse;
import com.leron.api.model.DTO.registerBank.CardResponse;
import com.leron.api.model.DTO.registerBank.RegisterBankRequest;
import com.leron.api.model.DTO.registerBank.RegisterBankResponse;
import com.leron.api.model.entities.Account;
import com.leron.api.model.entities.Bank;
import com.leron.api.model.entities.Card;
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

    public static void validateCard(Card card) throws ApplicationBusinessException {
        if (card == null) {
            throw new ApplicationBusinessException("ERROR", "CARD_NOT_FIND");
        }
    }

    public static void validateEditBank(Bank request, Bank current) throws ApplicationBusinessException {

        AtomicReference<Boolean> areAccountNumbersUnique = new AtomicReference<>(false);
        AtomicReference<Boolean> areCardNumbersUnique = new AtomicReference<>(false);

//        request.getAccounts().forEach(account -> {
//            if (request.getAccounts().stream()
//                    .anyMatch(reqAccount -> reqAccount.getAccountNumber().equals(account.getAccountNumber()))) {
//                areAccountNumbersUnique.set(true);
//            }
//
//            account.getCards().forEach(card -> {
//                request.getAccounts().forEach(reqAccount -> {
//                    if (reqAccount.getCards().stream()
//                            .anyMatch(reqCard -> reqCard.getFinalNumber().equals(card.getFinalNumber()))) {
//                        areCardNumbersUnique.set(true);
//                    }
//
//                });
//            });
//        });


        if (areAccountNumbersUnique.get()) {
            throw new ApplicationBusinessException("ERROR", "DUPLICATE_ACCOUNT_NUMBER");
        }

        if (areCardNumbersUnique.get()) {
            throw new ApplicationBusinessException("ERROR", "DUPLICATE_CARD_NUMBER");
        }

    }

    public static void validateEditAccount(AccountResponse request, Account current) throws ApplicationBusinessException {
        AtomicReference<Boolean> isSameBankName = new AtomicReference<>(false);
        AtomicReference<Boolean> areAccountNumbersUnique = new AtomicReference<>(false);
        AtomicReference<Boolean> areCardNumbersUnique = new AtomicReference<>(false);




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

    public static void validateEditCard(CardResponse request, Card current) throws ApplicationBusinessException {

        AtomicReference<Boolean> isSameBankName = new AtomicReference<>(false);
        AtomicReference<Boolean> areAccountNumbersUnique = new AtomicReference<>(false);
        AtomicReference<Boolean> areCardNumbersUnique = new AtomicReference<>(false);


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
