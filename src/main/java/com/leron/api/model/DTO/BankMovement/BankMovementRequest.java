package com.leron.api.model.DTO.BankMovement;

import com.leron.api.model.GenericEntities;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Component
@Data
public class BankMovementRequest {
    private Long userAuthId;
    private String type;
    private BigDecimal value;
    private Long ownerId;
    private Long bankId;
    private Long accountId;
    private Timestamp dateMovement;
    private String obs;
    private Long financialEntityCardId;
}
