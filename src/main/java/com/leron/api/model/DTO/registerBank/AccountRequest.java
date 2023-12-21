package com.leron.api.model.DTO.registerBank;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@Data
public class AccountRequest {
    private Long id;
    private String accountNumber;
    private String label;
    private String owner;
    private String value;
    private String currency;
    private List<CardRequest> cards;
}
