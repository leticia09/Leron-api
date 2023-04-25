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
            throw new ApplicationBusinessException("Lascou", "USER_ID_IS_EMPTY");
        }
        if(expenseRequest.getData().getShoppingDate() == null){
            throw new ApplicationBusinessException("Lascou", "SHOPPING_DATE_IS_EMPTY");
        }
        if(expenseRequest.getData().getLocal() == null){
            throw new ApplicationBusinessException("Lascou", "LOCAL_IS_EMPTY");
        }
        if(expenseRequest.getData().getGroup() == null){
            throw new ApplicationBusinessException("Lascou", "GROUP_IS_EMPTY");
        }
        if(expenseRequest.getData().getPrice() == null){
            throw new ApplicationBusinessException("Lascou", "PRICE_IS_EMPTY");
        }
        if(expenseRequest.getData().getFormPayment() == null){
            throw new ApplicationBusinessException("Lascou", "FORM_PAYMENT_IS_EMPTY");
        }
        if(expenseRequest.getData().getPayer() == null) {
            throw new ApplicationBusinessException("Lascou", "PAYER_IS_EMPTY");
        }

        if(expenseRequest.getData().getTypePayment() == null){
            throw new ApplicationBusinessException("Lascou", "TYPE_PAYMENT_IS_EMPTY");
        }
    }
}
