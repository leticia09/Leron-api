package com.leron.api.model.DTO.points;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Data
public class PointsRequest {
    private String program;
    private Long bankId;
    private Long accountId;
    private String currency;
    private BigDecimal point;
    private String status;
    private Long userAuthId;
}
