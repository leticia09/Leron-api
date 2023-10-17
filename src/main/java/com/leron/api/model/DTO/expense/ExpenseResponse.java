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
    private String description;
    private String type;
    private String userName;
    private Long userId;
    private java.util.Date shoppingDate;
    private String local;
    private String group;
    private BigDecimal price;
    private String formPayment;
    private String payer;
    private Long cardId;
    private String cardNickName;
    private Boolean advance;
    private String typePayment;
    private String status;
    private java.util.Date paymentDate;
    private Long installment;
    private Long userAuthId;
}
