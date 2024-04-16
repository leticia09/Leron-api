package com.leron.api.model.DTO.registerBank;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@Data
public class AccountResponse {
    private Long id;
    private String accountNumber;
    private Long owner;
    private String status;
    private BigDecimal value;
    private String currency;
    private List<CardResponse> cards;
}
