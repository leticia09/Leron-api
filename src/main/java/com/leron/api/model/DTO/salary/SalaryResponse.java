package com.leron.api.model.DTO.salary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SalaryResponse {
    private Long id;
    private String name;
    private Long userId;
    private String type;
    private String userName;
    private String status;
    private BigDecimal price;
}
