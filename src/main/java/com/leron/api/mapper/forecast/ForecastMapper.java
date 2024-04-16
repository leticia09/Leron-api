package com.leron.api.mapper.forecast;

import com.leron.api.model.DTO.expense.ExpenseResponse;
import com.leron.api.model.DTO.forecast.ForecastDateRequest;
import com.leron.api.model.DTO.forecast.ForecastPrevResponse;
import com.leron.api.model.DTO.forecast.ForecastRequest;
import com.leron.api.model.DTO.forecast.ForecastResponse;
import com.leron.api.model.entities.Forecast;
import com.leron.api.model.entities.ForecastDate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ForecastMapper {

    public static List<Forecast> requestToEntity(List<ForecastRequest> requestList) {
        List<Forecast> response = new ArrayList<>();

        requestList.forEach(res -> {
            Forecast forecast = new Forecast();
            forecast.setOwnerId(res.getOwnerId());
            forecast.setMacroGroupId(res.getMacroGroup());
            forecast.setSpecificGroupId(res.getSpecificGroup());
            forecast.setDeleted(false);
            forecast.setCreatedIn(new Date());
            forecast.setUserAuthId(res.getUserAuthId());
            forecast.setMacroGroupName(res.getMacroGroupName());
            forecast.setSpecificGroupName(res.getSpecificGroupName());
            forecast.setForecastDates(forecastDateRequestToEntity(res.getForecastDateRequestList()));
            response.add(forecast);
        });

        return response;
    }

    public static List<ForecastDate> forecastDateRequestToEntity(List<ForecastDateRequest> forecastDateRequestList) {
        List<ForecastDate> response = new ArrayList<>();
        forecastDateRequestList.forEach(forecastDateRequest -> {
            ForecastDate forecastDate = new ForecastDate();
            forecastDate.setValue(new BigDecimal(forecastDateRequest.getValue().replace(",", ".")));
            forecastDate.setMonth(forecastDateRequest.getMonth());
            forecastDate.setYear(forecastDateRequest.getYear());
            forecastDate.setCurrency(forecastDateRequest.getCurrency());
            response.add(forecastDate);
        });

        return response;
    }

    public static List<ForecastPrevResponse> entityToForecastPrevResponse(List<Forecast> forecastList, List<ExpenseResponse> expenseResponses) {
        List<ForecastPrevResponse> response = new ArrayList<>();
        forecastList.forEach(forecast -> {
            ForecastPrevResponse forecastResponse = new ForecastPrevResponse();
            forecastResponse.setForecastId(forecast.getId());
            forecastResponse.setOwnerId(forecast.getOwnerId());
            forecastResponse.setMacroGroup(forecast.getMacroGroupName());
            forecastResponse.setSpecificGroup(forecast.getSpecificGroupName());

            forecastResponse.setCurrency(forecast.getForecastDates().get(0).getCurrency());
            forecastResponse.setValueForecast(forecast.getForecastDates().get(0).getValue());

            List<ExpenseResponse> expenseList = expenseResponses.stream().filter(
                    expense -> Objects.nonNull(expense.getSpecificGroup()) &&
                            expense.getSpecificGroup().equalsIgnoreCase(forecast.getSpecificGroupName())
            ).collect(Collectors.toList());
            BigDecimal value = expenseList.stream()
                    .map(ExpenseResponse::getValue)
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            forecastResponse.setValuePaidForecast(value);

            forecastResponse.setDifference(forecastResponse.getValueForecast().subtract(value));

            response.add(forecastResponse);
        });
        return response;
    }

    public static List<ForecastResponse> entityToForecastResponse(List<ForecastDate> forecastDates) {
        List<ForecastResponse> response = new ArrayList<>();
        forecastDates.forEach(forecast -> {
            ForecastResponse forecastResponse = new ForecastResponse();
            forecastResponse.setId(forecast.getForecast().getId());
            forecastResponse.setOwnerId(forecast.getForecast().getOwnerId());
            forecastResponse.setMacroGroup(forecast.getForecast().getMacroGroupId());
            forecastResponse.setSpecificGroup(forecast.getForecast().getSpecificGroupId());
            forecastResponse.setMacroGroupName(forecast.getForecast().getMacroGroupName());
            forecastResponse.setSpecificGroupName(forecast.getForecast().getSpecificGroupName());
            forecastResponse.setForecastDataId(forecast.getId());
            forecastResponse.setCurrency(forecast.getCurrency());
            forecastResponse.setValue(String.valueOf(forecast.getValue()));
            forecastResponse.setMonth(forecast.getMonth());
            forecastResponse.setYear(forecast.getYear());
            response.add(forecastResponse);
        });
        return response;
    }
}
