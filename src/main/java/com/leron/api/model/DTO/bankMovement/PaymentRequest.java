package com.leron.api.model.DTO.bankMovement;

import com.leron.api.model.DTO.expense.ExpensePeriodResponse;
import lombok.Data;

import java.util.List;

@Data
public class PaymentRequest {
    private Long expenseId;
    private String paymentDate;
    private String referencePeriod;
    private String value;
    private Long ownerId;
    private String obs;
    private Long bankId;
    private Long accountId;
    private List<Long> cardId;
    private List<ExpensePeriodResponse> expenseList;

}
