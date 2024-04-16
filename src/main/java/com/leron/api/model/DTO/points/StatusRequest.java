package com.leron.api.model.DTO.points;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Data
@Component
public class StatusRequest {
    private Long programId;
    private BigDecimal value;
    private Long status;
    private Long userAuthId;
    private Long ownerId;

}
