package com.leron.api.model.DTO.expense;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExpenseRequest {
    private String userId;
    private String description;
    private String type;
    private java.util.Date shoppingDate;
    private String local;
    private String group;
    private String price;
    private Boolean advance;
    private String formPayment;
    private String payer;
    private String cardId;
    private String typePayment;
    private Long installment;
    private Long userAuthId;
}
