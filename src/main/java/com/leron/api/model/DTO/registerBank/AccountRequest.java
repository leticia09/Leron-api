package com.leron.api.model.DTO.registerBank;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
public class AccountRequest {
    private String accountNumber;
    private String label;
    private String owner;
    private List<CardRequest> cards;
}
