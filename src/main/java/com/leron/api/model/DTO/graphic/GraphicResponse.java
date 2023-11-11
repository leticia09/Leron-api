package com.leron.api.model.DTO.graphic;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@Data
public class GraphicResponse {
    private List<DataSet> dataSet;
    private ArrayList<String> labels;
    private BigDecimal totalMiles;
    private BigDecimal totalPoints;
    private BigDecimal totalProgramActive;
    private BigDecimal totalProgramInactive;
}
