package com.leron.api.model.DTO.forecast;

import com.leron.api.model.entities.Expense;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Data
@Component
public class ForecastPrevResponse {
    private Long ownerId;
    private String macroGroup;
    private String specificGroup;
    private BigDecimal valueForecast;
    private BigDecimal valuePaidForecast;
    private BigDecimal difference;
    private String currency;
    private List<Expense> expensesDetails;
}
