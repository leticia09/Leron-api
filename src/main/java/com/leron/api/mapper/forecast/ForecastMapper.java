package com.leron.api.mapper.forecast;

import com.leron.api.model.DTO.forecast.ForecastRequest;
import com.leron.api.model.DTO.forecast.ForecastResponse;
import com.leron.api.model.entities.Forecast;
import com.leron.api.responses.DataListResponse;
import com.leron.api.utils.FormatDate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ForecastMapper {

    public static List<Forecast> requestToEntity(List<ForecastRequest> requestList) {
        List<Forecast> response = new ArrayList<>();

        requestList.forEach(res -> {
            Forecast forecast = new Forecast();
            forecast.setCurrency(res.getCurrency());
            forecast.setMonths(res.getMonths());
            forecast.setValue(new BigDecimal(res.getValue().replace(",", ".")));
            forecast.setYears(res.getYears());
            forecast.setOwnerId(res.getOwnerId());
            forecast.setMacroGroupId(res.getMacroGroup());
            forecast.setHasFixed(res.getHasFixed());
            forecast.setSpecificGroupId(res.getSpecificGroup());
            forecast.setDeleted(false);
            forecast.setCreatedIn(new Date());
            forecast.setUserAuthId(res.getUserAuthId());
            response.add(forecast);
        });

        return response;
    }

    public static DataListResponse<ForecastResponse> entityToResponse(int month, int year, List<Forecast> forecastList, List<Long> owners) {
        DataListResponse<ForecastResponse> response = new DataListResponse<>();
        List<ForecastResponse> list = new ArrayList<>();
        List<Forecast> forecastListFiltered = forecastList.stream().filter(forecast ->
                (forecast.getMonths().contains(FormatDate.getMonth(month)) &&
                        forecast.getYears().contains(year) &&
                        owners.contains(forecast.getOwnerId())
                ) || forecast.getHasFixed()).collect(Collectors.toList());
        forecastListFiltered.forEach(forecast -> {
            ForecastResponse forecastResponse = new ForecastResponse();
            forecastResponse.setCurrency(forecast.getCurrency());
            forecastResponse.setValue(forecast.getValue());
            forecastResponse.setMonths(forecast.getMonths());
            forecastResponse.setYears(forecast.getYears());
            forecastResponse.setHasFixed(forecast.getHasFixed());
            forecastResponse.setOwnerId(forecast.getOwnerId());
            forecastResponse.setMacroGroup(forecast.getMacroGroupId());
            forecastResponse.setSpecificGroup(forecast.getSpecificGroupId());
            list.add(forecastResponse);
        });
        response.setData(list);
        return response;
    }

    public static DataListResponse<ForecastResponse> entityToResponse(List<Forecast> forecastList) {
        DataListResponse<ForecastResponse> response = new DataListResponse<>();
        List<ForecastResponse> list = new ArrayList<>();
        forecastList.forEach(forecast -> {
            ForecastResponse forecastResponse = new ForecastResponse();
            forecastResponse.setCurrency(forecast.getCurrency());
            forecastResponse.setValue(forecast.getValue());
            forecastResponse.setMonths(forecast.getMonths());
            forecastResponse.setYears(forecast.getYears());
            forecastResponse.setHasFixed(forecast.getHasFixed());
            forecastResponse.setOwnerId(forecast.getOwnerId());
            forecastResponse.setMacroGroup(forecast.getMacroGroupId());
            forecastResponse.setSpecificGroup(forecast.getSpecificGroupId());
            list.add(forecastResponse);
        });
        response.setData(list);
        return response;
    }
}
