package com.leron.api.validator.expense;

import com.leron.api.model.DTO.expense.ExpenseRequest;
import com.leron.api.model.DTO.user.UserRequest;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ExpenseValidator {
    public static void validatorExpense(DataRequest<ExpenseRequest> expenseRequest) throws ApplicationBusinessException {

        if(expenseRequest.getData().getUserId() == null){
            throw new ApplicationBusinessException("Lascou", "userId");
        }
        if(expenseRequest.getData().getShoppingDate() == null){
            throw new ApplicationBusinessException("Lascou", "ShoppingDate");
        }
        if(expenseRequest.getData().getLocal() == null){
            throw new ApplicationBusinessException("Lascou", "local");
        }
        if(expenseRequest.getData().getGroup() == null){
            throw new ApplicationBusinessException("Lascou", "group");
        }
        if(expenseRequest.getData().getPrice() == null){
            throw new ApplicationBusinessException("Lascou", "price");
        }
        if(expenseRequest.getData().getObs() == null){
            throw new ApplicationBusinessException("Lascou", "obs");
        }
        if(expenseRequest.getData().getFormPayment() == null){
            throw new ApplicationBusinessException("Lascou", "formPayment");
        }
        if(expenseRequest.getData().getPayer() == null){
            throw new ApplicationBusinessException("Lascou", "payer");
        }
        if(expenseRequest.getData().getCardId() == null){
            throw new ApplicationBusinessException("Lascou", "cardId");
        }
        if(expenseRequest.getData().getMethod() == null){
            throw new ApplicationBusinessException("Lascou", "method");
        }
        if(expenseRequest.getData().getTypePayment() == null){
            throw new ApplicationBusinessException("Lascou", "typePayment");
        }
        if(expenseRequest.getData().getStatus() == null){
            throw new ApplicationBusinessException("Lascou", "status");
        }
        if(expenseRequest.getData().getPaymentDate() == null){
            throw new ApplicationBusinessException("Lascou", "paymentDate");
        }
    }
}
