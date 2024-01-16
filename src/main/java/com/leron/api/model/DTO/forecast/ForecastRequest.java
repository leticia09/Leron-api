package com.leron.api.model.DTO.forecast;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class ForecastRequest {
    private Long id;
    private Long ownerId;
    private String value;
    private Long macroGroup;
    private Long specificGroup;
    private String currency;
    private List<String> months;
    private List<Integer> years;
    private Long userAuthId;
    private Boolean hasFixed;
}
