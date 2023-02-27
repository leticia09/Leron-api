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
    private BigDecimal price;
    private String status;
    private String type;
}
