package com.leron.api.model.DTO.registerBank;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
public class AccountResponse {
    private Long id;
    private String accountNumber;
    private String owner;
    private String status;
    private List<CardResponse> cards;
}
