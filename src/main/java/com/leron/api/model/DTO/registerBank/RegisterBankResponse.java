package com.leron.api.model.DTO.registerBank;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@Data
public class RegisterBankResponse {
    private Long id;
    private String name;
    private Long userAuthId;
    private List<AccountResponse> accounts;
}
