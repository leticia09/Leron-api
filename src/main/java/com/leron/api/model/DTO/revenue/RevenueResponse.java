package com.leron.api.model.DTO.revenue;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RevenueResponse {
    private Long id;
    private String description;
    private Long salaryId;
    private String salaryName;
    private String type;
    private java.util.Date receivingDate;
    private BigDecimal value;
    private Long bankId;
    private Long accountId;
    private String bankName;
    private String accountName;
    private String accountNumber;
    private String status;
    private Long userAuthId;
}
