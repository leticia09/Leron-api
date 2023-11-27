package com.leron.api.controller;

import com.leron.api.model.DTO.expense.ExpenseResponse;
import com.leron.api.model.DTO.points.UseRequest;
import com.leron.api.model.DTO.typeSalary.TypeSalaryRequest;
import com.leron.api.model.entities.TypeSalary;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataRequest;
import com.leron.api.responses.DataResponse;
import com.leron.api.service.typeSalary.TypeSalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/type-salary")
public class TypeSalaryController {
    @Autowired
    TypeSalaryService service;

    @GetMapping("/{userAuthId}")
    public List<TypeSalary> list(@PathVariable(value = "userAuthId", required = true) Long userAuthId) {
        return service.list(userAuthId);
    }

    @PostMapping(
            value = "",
            consumes = "application/json",
            produces = "application/json"
    )
    public DataResponse<TypeSalary> edit(
            @RequestBody List<TypeSalaryRequest> requestCreation,
            @RequestHeader(name = "locale", required = true) String locale,
            @RequestHeader(name = "Authorization", required = true) String authorization
    ) {
        DataRequest<List<TypeSalaryRequest>> request = new DataRequest<>(requestCreation, locale, authorization);
        DataResponse<TypeSalary> response = new DataResponse<>();
        try {
            service.edit(request);
            return response;

        } catch (ApplicationBusinessException error){
            response.setResponse(error);
        }
        return response;
    }
}
