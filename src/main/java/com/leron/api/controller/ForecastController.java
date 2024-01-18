package com.leron.api.controller;

import com.leron.api.model.DTO.forecast.ForecastRequest;
import com.leron.api.model.DTO.forecast.ForecastResponse;
import com.leron.api.model.DTO.graphic.GraphicResponse;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataResponse;
import com.leron.api.service.forecast.ForecastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/forecast")
public class ForecastController {
    @Autowired
    ForecastService forecastService;

    @PostMapping
    public DataResponse<ForecastResponse> createForecast(@RequestBody List<ForecastRequest> forecastRequest) {
        DataResponse<ForecastResponse> response = new DataResponse<>();
        try {
            response = forecastService.createForecast(forecastRequest);
        } catch (ApplicationBusinessException error) {
            response.setResponse(error);
        }
        return response;
    }

    @GetMapping("/{userAuthId}/{month}/{year}/{owners}")
    public DataListResponse<ForecastResponse> list(@PathVariable(value = "userAuthId", required = true) Long userAuthId,
                                                   @PathVariable(value = "month", required = true) int month,
                                                   @PathVariable(value = "year", required = true) int year,
                                                   @PathVariable(value = "owners", required = true) List<Long> owners) {
        return forecastService.list(userAuthId, month, year, owners);
    }

    @GetMapping("/{userAuthId}")
    public DataListResponse<ForecastResponse> listWithFilters(@PathVariable(value = "userAuthId", required = true) Long userAuthId) {
        return forecastService.list(userAuthId);
    }

    @GetMapping("data/{userAuthId}/{month}/{year}/{owners}")
    public DataResponse<GraphicResponse> getData(@PathVariable(value = "userAuthId", required = true) Long userAuthId,
                                                 @PathVariable(value = "month", required = true)int month,
                                                 @PathVariable(value = "year", required = true) int year,
                                                 @PathVariable(value = "owners", required = true) List<Long> owners) {
        return forecastService.getData(userAuthId, month,year, owners);
    }
}