package com.leron.api.model.DTO.expense;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExpenseResponse {
    private Long id;
    private String local;
    private String macroGroup;
    private String specificGroup;
    private Long ownerId;
    private String paymentForm;
    private String finalCard;
    private Long quantityPart;
    private Boolean hasFixed;
    private Timestamp dateBuy;
    private String obs;
    private BigDecimal value;
    private String status;
    private String currency;
    private int partNumber;
    private BigDecimal partValue;
    private BigDecimal valuePaid;
}
