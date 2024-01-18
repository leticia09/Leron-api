package com.leron.api.mapper.forecast;

import com.leron.api.model.DTO.forecast.ForecastPrevResponse;
import com.leron.api.model.DTO.forecast.ForecastRequest;
import com.leron.api.model.DTO.forecast.ForecastResponse;
import com.leron.api.model.entities.Expense;
import com.leron.api.model.entities.Forecast;
import com.leron.api.model.entities.MacroGroup;
import com.leron.api.model.entities.SpecificGroup;
import com.leron.api.responses.DataListResponse;
import com.leron.api.utils.FormatDate;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
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

    public static DataListResponse<ForecastPrevResponse> entityToResponsePrev(int month, int year, List<Forecast> forecastList, List<Expense> expensesFiltered, List<MacroGroup> macroGroupList) {
        DataListResponse<ForecastPrevResponse> response = new DataListResponse<>();
        List<ForecastPrevResponse> list = new ArrayList<>();

        for (Forecast forecast : forecastList) {
            if (forecast.getMonths().contains(FormatDate.getMonth(month)) && forecast.getYears().contains(year)) {
                ForecastPrevResponse forecastPrevResponse = new ForecastPrevResponse();
                forecastPrevResponse.setOwnerId(forecast.getOwnerId());
                forecastPrevResponse.setCurrency(forecast.getCurrency());
                Optional<MacroGroup> macroGroupOptional = macroGroupList.stream().filter(group -> group.getId().equals(forecast.getMacroGroupId())).findFirst();
                if (macroGroupOptional.isPresent()) {
                    forecastPrevResponse.setMacroGroup(macroGroupOptional.get().getName());
                    Optional<SpecificGroup> specificGroupOptional = macroGroupOptional.get().getSpecificGroups().stream().filter(sp -> sp.getId().equals(forecast.getSpecificGroupId())).findFirst();
                    if (specificGroupOptional.isPresent()) {
                        forecastPrevResponse.setSpecificGroup(specificGroupOptional.get().getName());
                        List<Expense> expenseList = expensesFiltered.stream().filter(expense -> Objects.nonNull(expense.getSpecificGroup()) && expense.getSpecificGroup().equalsIgnoreCase(specificGroupOptional.get().getName())).collect(Collectors.toList());
                        forecastPrevResponse.setExpensesDetails(expenseList);
                        BigDecimal value = expenseList.stream()
                                .map(Expense::getValue)
                                .reduce(BigDecimal::add)
                                .orElse(BigDecimal.ZERO);
                        forecastPrevResponse.setValuePaidForecast(value);

                        forecastPrevResponse.setDifference(forecast.getValue().subtract(value));
                    }
                }
                forecastPrevResponse.setValueForecast(forecast.getValue());

                list.add(forecastPrevResponse);
            }
        }

        response.setData(list);
        return response;
    }
}
