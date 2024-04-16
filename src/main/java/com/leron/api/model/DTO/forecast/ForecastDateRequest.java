package com.leron.api.model.DTO.forecast;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class ForecastDateRequest {
    private String month;
    private String currency;
    private String value;
    private Long year;
}
