package com.leron.api.model.DTO.expense;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Data
@Component
public class ObjectTemp {
    private String specificGroup;
    private String currency;
    private BigDecimal value;
}
