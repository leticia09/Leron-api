package com.leron.api.model.DTO.BankMovement;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Component
@Data
public class BankMovementResponse {
    private Long id;
    private String type;
    private BigDecimal value;
    private Long ownerId;
    private Long bankId;
    private Long accountId;
    private Timestamp dateMovement;
    private String obs;
    private Long entranceId;
    private Long expenseId;
    private String referencePeriod;
    private String currency;
}
