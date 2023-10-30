package com.leron.api.model.DTO.points;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Component
public class TransferRequest {
    private Long originProgramId;
    private Long destinyProgramId;
    private BigDecimal quantity;
    private Timestamp pointsExpirationDate;
    private BigDecimal originValue;
    private BigDecimal destinyValue;
    private BigDecimal bonus;
    private Long userAuthId;
}
