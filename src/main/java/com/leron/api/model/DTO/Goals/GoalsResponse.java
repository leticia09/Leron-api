package com.leron.api.model.DTO.Goals;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Component
@Data
public class GoalsResponse {
    private Long id;
    private String goal;
    private Long ownerId;
    private Long bankId;
    private String accountNumber;
    private Timestamp openDate;
    private Integer validityInMonths;
    private BigDecimal value;
    private String currency;
    private String profitabilityMonthly;
    private String goalPreference;
    private BigDecimal partValue;
    private BigDecimal fees;
    private BigDecimal contributionTotal;
    private String status;
    private BigDecimal contributionValuePaid;
    private Integer contributionNumber;
    private String bankName;
}
