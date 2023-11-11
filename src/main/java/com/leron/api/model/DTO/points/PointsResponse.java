package com.leron.api.model.DTO.points;

import com.leron.api.model.entities.Member;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Component
public class PointsResponse {
    private Long id;
    private String program;
    private String status;
    private Long userAuthId;
    private BigDecimal value;
    private Timestamp pointsExpirationDate;
    private String typeOfScore;
    private Member owner;
}