package com.leron.api.validator.bankAccount;

import com.leron.api.model.DTO.bankAccount.BankAccountRequest;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataRequest;
import org.springframework.stereotype.Component;

@Component
public class BankAccountValidator {
    public static void validatorBankAccount(DataRequest<BankAccountRequest> bankRequest) throws ApplicationBusinessException {
        if(bankRequest.getData().getStatus() == null){
            throw new ApplicationBusinessException("Lascou", "STATUS");
        }
        if(bankRequest.getData().getIdBank() == null){
            throw new ApplicationBusinessException("Lascou", "ID BANK");
        }
        if(bankRequest.getData().getAccountNumber() == null){
            throw new ApplicationBusinessException("Lascou", "ACCOUNT NUMBER");
        }
        if(bankRequest.getData().getIdUser() == null){
            throw new ApplicationBusinessException("Lascou", "ID USER");
        }
        if(bankRequest.getData().getNickName() == null){
            throw new ApplicationBusinessException("Lascou", "NICKNAME");
        }


    }
}
