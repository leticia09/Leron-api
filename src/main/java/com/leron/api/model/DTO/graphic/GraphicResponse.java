package com.leron.api.model.DTO.graphic;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;

@Component
@Data
public class GraphicResponse {
    private ArrayList<String> labels;
    private ArrayList<BigDecimal> data;
    private BigDecimal totalMiles;
    private BigDecimal totalPoints;
    private BigDecimal totalProgramActive;
    private BigDecimal totalProgramInactive;
}
