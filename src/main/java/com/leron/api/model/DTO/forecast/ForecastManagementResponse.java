package com.leron.api.model.DTO.forecast;

import com.leron.api.model.DTO.graphic.GraphicResponse;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class ForecastManagementResponse {
    GraphicResponse graphicResponse;
    List<ForecastPrevResponse> forecastPrevResponseList;
    List<ForecastResponse> forecastResponseList;
}
