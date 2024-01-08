package com.leron.api.model.DTO.BankMovement;

import lombok.Data;

@Data
public class PaymentRequest {
    private Long expenseId;
    private String paymentDate;
    private String referencePeriod;
    private String value;
    private Long ownerId;
    private String obs;
}
