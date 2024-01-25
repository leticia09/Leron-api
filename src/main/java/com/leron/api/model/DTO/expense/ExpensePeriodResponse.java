package com.leron.api.model.DTO.expense;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExpensePeriodResponse {
    private Long id;
    private String local;
    private String currency;
    private Long ownerId;
    private Long finalCard;
    private String value;
}
