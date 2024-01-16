package com.leron.api.model.DTO.forecast;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Data
@Component
public class ForecastResponse {
    private Long id;
    private Long ownerId;
    private BigDecimal value;
    private Long macroGroup;
    private Long specificGroup;
    private String currency;
    private List<String> months;
    private List<Integer> years;
    private Boolean hasFixed;
}
