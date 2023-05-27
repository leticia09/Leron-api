package com.leron.api.model.DTO.points;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Data
@Component
public class PointsResponse {
    private Long id;
    private String program;
    private Long bankId;
    private Long accountId;
    private String bankName;
    private String accountName;
    private String accountNumber;
    private String currency;
    private BigDecimal point;
    private String status;
    private Long userAuthId;
}