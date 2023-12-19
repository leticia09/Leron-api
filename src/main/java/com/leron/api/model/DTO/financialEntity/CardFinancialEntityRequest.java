package com.leron.api.model.DTO.financialEntity;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Data
@Component
public class CardFinancialEntityRequest {
    private String entityName;
    private String cardName;
    private Long finalCard;
    private String modality;
    private String balance;
    private Long ownerId;
    private String currency;
}
