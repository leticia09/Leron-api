package com.leron.api.model.DTO.points;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Component
@Data
public class PointsRequest {
    private String program;
    private Long userAuthId;
    private BigDecimal value;
    private Timestamp pointsExpirationDate;
    private String typeOfScore;
}
