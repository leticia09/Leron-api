package com.leron.api.model.DTO.Goals;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class GoalsRequest {
    private String goal;
    private Long ownerId;
    private Long bankId;
    private String accountNumber;
    private String openDate;
    private Integer validityInMonths;
    private Long userAuthId;
    private String value;
    private String currency;
    private String profitabilityMonthly;
    private String goalPreference;
    private String partValue;
}
