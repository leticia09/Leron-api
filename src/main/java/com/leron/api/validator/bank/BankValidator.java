package com.leron.api.validator.bank;

import com.leron.api.model.DTO.bank.BankRequest;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataRequest;
import org.springframework.stereotype.Component;

@Component
public class BankValidator {
    public static void validatorBank(DataRequest<BankRequest> bankRequest) throws ApplicationBusinessException {
        if(bankRequest.getData().getStatus() == null){
            throw new ApplicationBusinessException("Lascou", "STATUS");
        }
        if(bankRequest.getData().getName() == null){
            throw new ApplicationBusinessException("Lascou", "NOME");
        }


    }
}
