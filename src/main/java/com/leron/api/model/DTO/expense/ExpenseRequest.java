package com.leron.api.model.DTO.expense;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExpenseRequest {
    private String userId;
    private String nickName;
    private String type;
    private java.util.Date shoppingDate;
    private String local;
    private String group;
    private String price;
    private String obs;
    private String method;
    private String formPayment;
    private String payer;
    private String cardId;
    private String typePayment;
    private String status; // SETAR NA MAPPER
    private String paymentDate; // SETAR NA MAPPER
}
