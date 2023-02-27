package com.leron.api.mapper.expense;

import com.leron.api.model.DTO.expense.ExpenseRequest;
import com.leron.api.model.DTO.expense.ExpenseResponse;
import com.leron.api.model.entities.CardEntity;
import com.leron.api.model.entities.ExpenseEntity;
import com.leron.api.model.entities.UserEntity;
import com.leron.api.responses.DataListResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExpenseMapper {

    public static DataListResponse<ExpenseResponse> expenseEntitiesToDataListResponse(List<ExpenseEntity> expenseEntities, List<CardEntity> cardEntities,  List<UserEntity> userEntityList){
        DataListResponse<ExpenseResponse> response = new DataListResponse<>();
        List<ExpenseResponse> responseList = new ArrayList<>();

        for (ExpenseEntity expense : expenseEntities) {
            ExpenseResponse expenseResponse = new ExpenseResponse();

            cardEntities.forEach(card -> {
                if(card.getId().equals(expense.getCardId())){
                    expenseResponse.setCardNickName(card.getNickName());
                }
            });

            userEntityList.forEach(user -> {
                if(user.getId().equals(expense.getUserId())){
                    expenseResponse.setUserName(user.getName());
                }
            });

            expenseResponse.setId(expense.getId());
            expenseResponse.setType(expense.getType());
            expenseResponse.setUserId(expense.getUserId());
            expenseResponse.setNickName(expense.getNickName());
            expenseResponse.setShoppingDate(expense.getShoppingDate());
            expenseResponse.setLocal(expense.getLocal());
            expenseResponse.setGroup(expense.getGroup());
            expenseResponse.setPrice(expense.getPrice());
            expenseResponse.setObs(expense.getObs());
            expenseResponse.setFormPayment(expense.getFormPayment());
            expenseResponse.setPayer(expense.getPayer());
            expenseResponse.setCardId(expense.getCardId());
            expenseResponse.setMethod(expense.getMethod());
            expenseResponse.setTypePayment(expense.getTypePayment());
            expenseResponse.setStatus(expense.getStatus());
            expenseResponse.setPaymentDate(expense.getPaymentDate());

            responseList.add(expenseResponse);
        }
        response.setData(responseList);
        return response;
    }

    public static ExpenseEntity createExpenseFromExpenseRequest(ExpenseRequest expenseRequest) {
        ExpenseEntity expenseEntity = new ExpenseEntity();

        expenseEntity.setUserId(Long.valueOf(expenseRequest.getUserId()));
        expenseEntity.setNickName(expenseRequest.getNickName());
        expenseEntity.setType(expenseRequest.getType());
        expenseEntity.setShoppingDate(expenseRequest.getShoppingDate());
        expenseEntity.setLocal(expenseRequest.getLocal());
        expenseEntity.setGroup(expenseRequest.getGroup());
        expenseEntity.setPrice(new BigDecimal(expenseRequest.getPrice()));
        expenseEntity.setObs(expenseRequest.getObs());
        expenseEntity.setFormPayment(expenseRequest.getFormPayment());
        expenseEntity.setPayer(expenseRequest.getPayer());
        expenseEntity.setCardId(Long.valueOf(expenseRequest.getCardId()));
        expenseEntity.setMethod(expenseRequest.getMethod());
        expenseEntity.setTypePayment(expenseRequest.getTypePayment());
        expenseEntity.setStatus(expenseRequest.getStatus());
        expenseEntity.setPaymentDate(expenseRequest.getPaymentDate());

        return expenseEntity;
    }

    public static ExpenseResponse createExpenseResponse (ExpenseEntity expenseEntity) {

        ExpenseResponse expenseResponse = new ExpenseResponse();
        expenseResponse.setUserId(expenseEntity.getUserId());
        expenseResponse.setType(expenseEntity.getType());
        expenseResponse.setNickName(expenseEntity.getNickName());
        expenseResponse.setShoppingDate(expenseEntity.getShoppingDate());
        expenseResponse.setLocal(expenseEntity.getLocal());
        expenseResponse.setGroup(expenseEntity.getGroup());
        expenseResponse.setPrice(expenseEntity.getPrice());
        expenseResponse.setObs(expenseEntity.getObs());
        expenseResponse.setFormPayment(expenseEntity.getFormPayment());
        expenseResponse.setPayer(expenseEntity.getPayer());
        expenseResponse.setCardId(expenseEntity.getCardId());
        expenseResponse.setMethod(expenseEntity.getMethod());
        expenseResponse.setTypePayment(expenseEntity.getTypePayment());
        expenseResponse.setStatus(expenseEntity.getStatus());
        expenseResponse.setPaymentDate(expenseEntity.getPaymentDate());

        return expenseResponse;
    }
}
