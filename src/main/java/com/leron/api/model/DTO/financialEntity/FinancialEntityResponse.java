package com.leron.api.model.DTO.financialEntity;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class FinancialEntityResponse {
    private Long id;
    private String name;
    private List<CardFinancialEntityResponse> cardFinancialEntityResponseList;
}
