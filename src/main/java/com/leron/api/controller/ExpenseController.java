package com.leron.api.controller;

import com.leron.api.model.DTO.expense.ExpenseRequest;
import com.leron.api.model.DTO.expense.ExpenseResponse;
import com.leron.api.model.DTO.graphic.GraphicResponse;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataResponse;
import com.leron.api.service.expense.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/expense")
public class ExpenseController {

    @Autowired
    ExpenseService service;

    @PostMapping
    public DataResponse<ExpenseResponse> create(@RequestBody List<ExpenseRequest> requestDTO,
                                                @RequestHeader(name = "locale", required = false) String locale,
                                                @RequestHeader(name = "Authorization", required = false) String authorization) throws ApplicationBusinessException {

        DataResponse<ExpenseResponse> response = new DataResponse<>();
        try {
            response = service.create(requestDTO);
        } catch (ApplicationBusinessException error) {
            response.setResponse(error);
        }
        return response;

    }

    @GetMapping("/{userAuthId}/{month}/{year}")
    public DataListResponse<ExpenseResponse> list(@PathVariable(value = "userAuthId", required = true) Long userAuthId,
                                                   @PathVariable(value = "month", required = true) int month,
                                                   @PathVariable(value = "year", required = true) int year) {
        return service.list(userAuthId, month, year);
    }

    @GetMapping("/{userAuthId}")
    public DataListResponse<ExpenseResponse> listWithoutFilters(@PathVariable(value = "userAuthId", required = true) Long userAuthId) {
        return service.list(userAuthId);
    }

    @GetMapping("data/{userAuthId}/{month}/{year}")
    public DataResponse<GraphicResponse> getProgramsData(@PathVariable(value = "userAuthId", required = true) Long userAuthId,
                                                         @PathVariable(value = "month", required = true) int month,
                                                         @PathVariable(value = "year", required = true) int year) {
        return service.getData(userAuthId, month, year);
    }
}
