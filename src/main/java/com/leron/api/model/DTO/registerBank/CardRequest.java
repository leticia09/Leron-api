package com.leron.api.model.DTO.registerBank;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Component
@Data
public class CardRequest {
    private String name;
    private Long owner;
    private Long finalNumber;
    private String modality;
    private Integer closingDate;
    private Integer dueDate;
    private BigDecimal points;
    private String currency;
    private Long program;
}
