package com.leron.api.model.DTO.registerBank;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@Data
public class AccountRequest {
    private String accountNumber;
    private String label;
    private Long owner;
    private BigDecimal value;
    private String currency;
    private List<CardRequest> cards;
}
