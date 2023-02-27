package com.leron.api.model.DTO.expense;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExpenseResponse {
    private Long id;
    private String nickName;
    private String type;
    private String userName;
    private Long userId;
    private java.util.Date shoppingDate;
    private String local;
    private String group;
    private BigDecimal price;
    private String obs;
    private String formPayment;
    private String payer;
    private Long cardId;
    private String cardNickName;
    private String method;
    private String typePayment;
    private String status;
    private String paymentDate;
}
