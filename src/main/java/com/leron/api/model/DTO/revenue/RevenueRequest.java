package com.leron.api.model.DTO.revenue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RevenueRequest {
    private String description;
    private Long salaryId;
    private String type;
    private java.util.Date receivingDate;
    private BigDecimal value;
    private Long bankId;
    private Long accountId;
    private String status;
    private Long userAuthId;
}
