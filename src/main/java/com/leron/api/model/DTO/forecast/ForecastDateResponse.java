package com.leron.api.model.DTO.forecast;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Data
@Component
public class ForecastDateResponse {
    private Long id;
    private int month;
    private String currency;
    private String value;
    private Long year;
}
