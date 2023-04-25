package com.leron.api.validator.bankAccount;

import com.leron.api.model.DTO.bankAccount.BankAccountRequest;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataRequest;
import org.springframework.stereotype.Component;

@Component
public class BankAccountValidator {
    public static void validatorBankAccount(DataRequest<BankAccountRequest> bankRequest) throws ApplicationBusinessException {
        if(bankRequest.getData().getStatus() == null || bankRequest.getData().getStatus().isEmpty()){
            throw new ApplicationBusinessException("Lascou", "STATUS_IS_EMPTY");
        }
        if(bankRequest.getData().getIdBank() == null){
            throw new ApplicationBusinessException("Lascou", "ID_BANK_IS_EMPTY");
        }
        if(bankRequest.getData().getAccountNumber() == null || bankRequest.getData().getAccountNumber().isEmpty()) {
            throw new ApplicationBusinessException("Lascou", "ACCOUNT_NUMBER_IS_EMPTY");
        }
        if(bankRequest.getData().getIdUser() == null){
            throw new ApplicationBusinessException("Lascou", "ID_USER_IS_EMPTY");
        }
        if(bankRequest.getData().getNickName() == null || bankRequest.getData().getNickName().isEmpty()){
            throw new ApplicationBusinessException("Lascou", "NICKNAME_IS_EMPTY");
        }


    }
}
