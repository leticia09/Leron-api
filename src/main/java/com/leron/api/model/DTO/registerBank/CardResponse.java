package com.leron.api.model.DTO.registerBank;

import com.leron.api.model.entities.Member;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Component
@Data
public class CardResponse {
    private Long id;
    private String name;
    private Member owner;
    private String status;
    private Long finalNumber;
    private String modality;
    private Integer closingDate;
    private Integer dueDate;
    private BigDecimal point;
    private String currencyPoint;
    private BigDecimal value;
    private Timestamp pointsExpirationDate;
}
