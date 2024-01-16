package com.leron.api.validator.forecast;

import com.leron.api.model.DTO.forecast.ForecastRequest;
import com.leron.api.model.entities.Forecast;
import com.leron.api.responses.ApplicationBusinessException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class ValidatorForecast {

    public static void validateCreation(List<ForecastRequest> requestList, List<Forecast> forecastCurrent) throws ApplicationBusinessException {
        AtomicReference<Boolean> exists = new AtomicReference<>(false);


        if (exists.get()) {
            throw new ApplicationBusinessException("ERROR", "FORECAST_ALREADY_EXISTS");
        }
    }
}
