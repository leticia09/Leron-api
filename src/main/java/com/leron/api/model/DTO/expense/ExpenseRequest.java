package com.leron.api.model.DTO.expense;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExpenseRequest {
    private String local;
    private String macroGroup;
    private String specificGroup;
    private Long ownerId;
    private String paymentForm;
    private Long finalCard;
    private Long quantityPart;
    private Boolean hasFixed;
    private String dateBuy;
    private String obs;
    private String value;
    private Long userAuthId;
    private Long index;

    private Long moneyId;
    private Long ticketId;
    private Long cardId;
    private Boolean hasSplitExpense;
    private String frequency;
    private String initialDate;
    private Long monthPayment;
    private Long dayPayment;
}
