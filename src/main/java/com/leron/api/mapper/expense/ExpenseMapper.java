package com.leron.api.mapper.expense;

import com.leron.api.model.DTO.expense.ExpenseRequest;
import com.leron.api.model.DTO.expense.ExpenseResponse;
import com.leron.api.model.entities.*;
import com.leron.api.responses.DataListResponse;
import com.leron.api.utils.FormatDate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class ExpenseMapper {

    public static List<Expense> requestToEntity(List<ExpenseRequest> requestList) {
        List<Expense> response = new ArrayList<>();

        requestList.forEach(res -> {
            Expense expense = new Expense();

            expense.setLocal(res.getLocal());
            expense.setMacroGroup(res.getMacroGroup());
            expense.setOwnerId(res.getOwnerId());
            expense.setPaymentForm(res.getPaymentForm());
            expense.setHasFixed(res.getHasFixed());
            expense.setDateBuy(FormatDate.formatDate((res.getDateBuy())));
            expense.setUserAuthId(res.getUserAuthId());
            expense.setValue(new BigDecimal(res.getValue().replace(",", ".")));
            expense.setCreatedIn(new Date());
            expense.setDeleted(false);
            if(Objects.nonNull(res.getCardId())) {
                expense.setCardId(res.getCardId());
            }
            if(Objects.nonNull(res.getQuantityPart())) {
                expense.setQuantityPart(res.getQuantityPart());
            }
            if(Objects.nonNull(res.getObs())) {
                expense.setObs(res.getObs());
            }

            response.add(expense);
        });

        return response;
    }

    public static DataListResponse<ExpenseResponse> entityToResponse(List<Expense> expenses, List<Member> members, List<Bank> banks,  List<BankMovement>  bankMovements, int month, int year) {
        DataListResponse<ExpenseResponse> response = new DataListResponse<>();
        List<ExpenseResponse> entranceList = new ArrayList<>();
        for (Expense expense : expenses) {
            ExpenseResponse expenseResponse = new ExpenseResponse();
            expenseResponse.setId(expense.getId());
            expenseResponse.setLocal(expense.getLocal());
            expenseResponse.setMacroGroup(expense.getMacroGroup());
            expenseResponse.setOwnerId(expense.getOwnerId());
            expenseResponse.setPaymentForm(expense.getPaymentForm());
            expenseResponse.setHasFixed(expense.getHasFixed());
            expenseResponse.setDateBuy(expense.getDateBuy());
            expenseResponse.setValue(expense.getValue());
            if(Objects.nonNull(expense.getObs())) {
                expenseResponse.setObs(expense.getObs());
            }
            if(Objects.nonNull(expense.getCardId())) {
                expenseResponse.setCardId(expense.getCardId());
            }
            if(Objects.nonNull(expense.getQuantityPart())) {
                expenseResponse.setQuantityPart(expense.getQuantityPart());
            }

        }
        response.setData(entranceList);
        return response;
    }

    public static DataListResponse<ExpenseResponse> entityToResponse(List<Expense> expenses, List<Member> members, List<Bank> banks,  List<BankMovement>  bankMovements) {
        DataListResponse<ExpenseResponse> response = new DataListResponse<>();
        List<ExpenseResponse> entranceList = new ArrayList<>();
        for (Expense expense : expenses) {
            ExpenseResponse expenseResponse = new ExpenseResponse();
            expenseResponse.setId(expense.getId());
            expenseResponse.setLocal(expense.getLocal());
            expenseResponse.setMacroGroup(expense.getMacroGroup());
            expenseResponse.setOwnerId(expense.getOwnerId());
            expenseResponse.setPaymentForm(expense.getPaymentForm());
            expenseResponse.setHasFixed(expense.getHasFixed());
            expenseResponse.setDateBuy(expense.getDateBuy());
            expenseResponse.setValue(expense.getValue());
            if(Objects.nonNull(expense.getObs())) {
                expenseResponse.setObs(expense.getObs());
            }
            if(Objects.nonNull(expense.getCardId())) {
                expenseResponse.setCardId(expense.getCardId());
            }
            if(Objects.nonNull(expense.getQuantityPart())) {
                expenseResponse.setQuantityPart(expense.getQuantityPart());
            }

        }
        response.setData(entranceList);
        return response;
    }

}
