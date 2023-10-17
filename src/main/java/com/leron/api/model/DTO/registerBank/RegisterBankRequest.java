package com.leron.api.model.DTO.registerBank;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
public class RegisterBankRequest {
    private String name;
    private Long userAuthId;
    private List<AccountRequest> accounts;
}
