package com.leron.api.validator.revenue;

import com.leron.api.model.DTO.macroGroup.MacroGroupRequest;
import com.leron.api.model.DTO.revenue.RevenueRequest;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class RevenueValidator {
    public static void validator(DataRequest<RevenueRequest> request) throws ApplicationBusinessException {
        if(request.getData().getDescription() == null || request.getData().getDescription().isEmpty()){
            throw new ApplicationBusinessException("Lascou", "DESCRIPTION_IS_EMPTY");
        }
        if(request.getData().getSalaryId() == null){
            throw new ApplicationBusinessException("Lascou", "SALARY_IS_EMPTY");
        }
        if(request.getData().getType() == null || request.getData().getType().isEmpty()){
            throw new ApplicationBusinessException("Lascou", "TYPE_IS_EMPTY");
        }
        if(request.getData().getReceivingDate() == null){
            throw new ApplicationBusinessException("Lascou", "RECEIVING_DATE_IS_EMPTY");
        }
        if(request.getData().getValue() == null){
            throw new ApplicationBusinessException("Lascou", "VALUE_IS_EMPTY");
        }
        if(request.getData().getBankId() == null){
            throw new ApplicationBusinessException("Lascou", "BANK_IS_EMPTY");
        }
        if(request.getData().getAccountId() == null){
            throw new ApplicationBusinessException("Lascou", "ACCOUNT_IS_EMPTY");
        }
        if(request.getData().getStatus() == null || request.getData().getStatus().isEmpty()){
            throw new ApplicationBusinessException("Lascou", "STATUS_IS_EMPTY");
        }
    }
}
