package com.leron.api.validator.expense;

import com.leron.api.model.DTO.expense.ExpenseRequest;
import com.leron.api.model.entities.Expense;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ValidatorExpense {
    public static void validatorCreation(List<ExpenseRequest> expenseRequest, List<Expense> current) throws ApplicationBusinessException {


    }
}
