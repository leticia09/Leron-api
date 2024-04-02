package com.leron.api.model.DTO.forecast;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class ForecastResponse {
    private Long id;
    private Long ownerId;
    private Long macroGroup;
    private Long specificGroup;
    private String macroGroupName;
    private String specificGroupName;
    private List<ForecastDateResponse> forecastDateResponseList;
}