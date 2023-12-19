package com.leron.api.model.DTO.financialEntity;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class FinancialEntityRequest {
    private String name;
    private List<CardFinancialEntityRequest> cardFinancialEntityRequestList;
    private Long userAuthId;
}
