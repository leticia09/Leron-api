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
    private BigDecimal total1;
    private BigDecimal total2;
    private BigDecimal total3;
    private BigDecimal total4;
    private List<LabelTooltip> tooltipLabel;

}


