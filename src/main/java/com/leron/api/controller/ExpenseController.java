package com.leron.api.controller;

import com.leron.api.model.DTO.expense.ExpenseManagementResponse;
import com.leron.api.model.DTO.expense.ExpensePeriodResponse;
import com.leron.api.model.DTO.expense.ExpenseRequest;
import com.leron.api.model.DTO.expense.ExpenseResponse;
import com.leron.api.model.DTO.graphic.GraphicResponse;
import com.leron.api.model.entities.Expense;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataResponse;
import com.leron.api.service.expense.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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

    @GetMapping("/{userAuthId}/{month}/{year}/{owners}")
    public DataResponse<ExpenseManagementResponse> list(@PathVariable(value = "userAuthId", required = true) Long userAuthId,
                                                            @PathVariable(value = "month", required = true) int month,
                                                            @PathVariable(value = "year", required = true) int year,
                                                            @PathVariable(value = "owners", required = true) List<Long> owners) {
        return service.getManagementData(userAuthId, month, year, owners);
    }

    @GetMapping("period/{userAuthId}/{month}/{year}/{owner}/{cards}")
    public DataListResponse<ExpensePeriodResponse> listPeriod(@PathVariable(value = "userAuthId", required = true) Long userAuthId,
                                                              @PathVariable(value = "month", required = true) int month,
                                                              @PathVariable(value = "year", required = true) int year,
                                                              @PathVariable(value = "owner", required = true) Long owner,
                                                              @PathVariable(value = "cards", required = true) List<Long> cards) {
        return service.listPeriod(userAuthId, month + "/" + year, owner, cards);
    }

    @PostMapping("/{userAuthId}/{bankId}/{accountId}/{month}/{year}")
    public DataResponse<BigDecimal> getByRegisterBank(@PathVariable(value = "userAuthId", required = true) Long userAuthId,
                                                      @PathVariable(value = "bankId", required = true) Long bankId,
                                                      @PathVariable(value = "accountId", required = true) Long accountId,
                                                      @PathVariable(value = "month", required = true) String month,
                                                      @PathVariable(value = "year", required = true) String year,
                                                      @RequestBody List<String> cardListRequest) {
        return service.getAmountByRegisterBank(userAuthId, bankId, accountId, cardListRequest, month + "/" + year);
    }

    @GetMapping("/{userAuthId}")
    public DataListResponse<ExpenseResponse> listWithoutFilters(@PathVariable(value = "userAuthId", required = true) Long userAuthId) {
        return service.list(userAuthId);
    }

    @GetMapping("id/{id}")
    public Expense getById(@PathVariable(value = "id", required = true) Long id) {
        return service.getById(id);
    }

    @GetMapping("fixed/{userAuthId}/{month}/{year}/{owner}")
    public List<Expense> getExpenseFixed(
            @PathVariable(value = "userAuthId", required = true) Long userAuthId,
            @PathVariable(value = "month", required = true) int month,
            @PathVariable(value = "year", required = true) int year,
            @PathVariable(value = "owner", required = true) Long owner) {
        return service.getExpenseFixed(userAuthId, month, year, owner);
    }

    @GetMapping("split/{userAuthId}/{month}/{year}/{owner}")
    public List<Expense> getExpenseHasSplit(@PathVariable(value = "userAuthId", required = true) Long userAuthId,
                                            @PathVariable(value = "month", required = true) int month,
                                            @PathVariable(value = "year", required = true) int year,
                                            @PathVariable(value = "owner", required = true) Long owner) {
        return service.getExpenseHasSplit(userAuthId, month, year, owner);
    }
}
