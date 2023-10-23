package com.leron.api.model.DTO.salary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SalaryRequest {
    private String name;
    private Long userId;
    private String type;
    private BigDecimal grossSalaryValue;
    private Long userAuthId;
    private Long accountNumber;
    private String origin;
    private BigDecimal discount;
}
