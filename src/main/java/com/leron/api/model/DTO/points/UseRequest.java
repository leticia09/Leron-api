package com.leron.api.model.DTO.points;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Data
@Component
public class UseRequest {
    private Long programId;
    private BigDecimal value;
    private Long userAuthId;
}
