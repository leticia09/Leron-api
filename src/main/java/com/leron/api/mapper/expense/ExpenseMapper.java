package com.leron.api.mapper.expense;

import com.leron.api.model.DTO.expense.ExpenseRequest;
import com.leron.api.model.DTO.expense.ExpenseResponse;
import com.leron.api.model.entities.*;
import com.leron.api.responses.DataListResponse;
import com.leron.api.utils.FormatDate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Component
public class ExpenseMapper {

    public static List<Expense> requestToEntity(List<ExpenseRequest> requestList, Account account) {
        List<Expense> response = new ArrayList<>();

        requestList.forEach(res -> {
            Expense expense = new Expense();
            expense.setAccountId(account.getId());
            expense.setBankId(account.getBank().getId());
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
            if(Objects.nonNull(res.getFinalCard())) {
                expense.setFinalCard(res.getFinalCard());
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
    public static Expense requestToEntity(ExpenseRequest res,  Account account) {
            Expense expense = new Expense();
            expense.setAccountId(account.getId());
            expense.setBankId(account.getBank().getId());
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
            if(Objects.nonNull(res.getFinalCard())) {
                expense.setFinalCard(res.getFinalCard());
            }
            if(Objects.nonNull(res.getQuantityPart())) {
                expense.setQuantityPart(res.getQuantityPart());
            }
            if(Objects.nonNull(res.getObs())) {
                expense.setObs(res.getObs());
            }
            if(Objects.nonNull(res.getSpecificGroup())) {
                expense.setSpecificGroup(res.getSpecificGroup());
            }

        return expense;
    }
    public static DataListResponse<ExpenseResponse> entityToResponse(List<Expense> expenses, List<Member> members, List<Card> cards,  List<BankMovement>  bankMovements, int month, int year) {
        DataListResponse<ExpenseResponse> response = new DataListResponse<>();
        List<ExpenseResponse> expenseList = new ArrayList<>();
        for (Expense expense : expenses) {
            LocalDate currentDate = LocalDate.now();
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

            if(Objects.nonNull(expense.getQuantityPart())) {
                expenseResponse.setQuantityPart(expense.getQuantityPart());
            }
            if(Objects.nonNull(expense.getSpecificGroup())) {
                expenseResponse.setSpecificGroup(expense.getSpecificGroup());
            }

            Optional<BankMovement> bankMovement = bankMovements.stream().filter(bm -> Objects.nonNull(bm.getExpenseId()) && bm.getExpenseId().equals(expense.getId())).findFirst();
            Optional<Card> cardOptional = cards.stream().filter(ca -> ca.getFinalNumber().equals(expense.getFinalCard())).findFirst();

            cardOptional.ifPresent(card -> expenseResponse.setFinalCard(card.getName() + "/ " + expense.getFinalCard()));

            if(bankMovement.isPresent()) {
                expenseResponse.setStatus("Confirmado");
            } else {
                if(expense.getPaymentForm().equalsIgnoreCase("crédito")) {
                    if(cardOptional.isPresent() && cardOptional.get().getClosingDate() < currentDate.getDayOfMonth()) {
                        expenseResponse.setStatus("Aguardando");
                    } else {
                        expenseResponse.setStatus("Pendente");
                    }
                }
            }

            expenseList.add(expenseResponse);
        }
        response.setData(expenseList);
        return response;
    }

    public static DataListResponse<ExpenseResponse> entityToResponse(List<Expense> expenses, List<Member> members, List<Bank> banks,  List<BankMovement>  bankMovements) {
        DataListResponse<ExpenseResponse> response = new DataListResponse<>();
        List<ExpenseResponse> expenseList = new ArrayList<>();
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
            if(Objects.nonNull(expense.getFinalCard())) {
                expenseResponse.setFinalCard(expense.getFinalCard().toString());
            }
            if(Objects.nonNull(expense.getQuantityPart())) {
                expenseResponse.setQuantityPart(expense.getQuantityPart());
            }
            expenseList.add(expenseResponse);
        }
        response.setData(expenseList);
        return response;
    }

}
