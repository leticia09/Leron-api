package com.leron.api.controller;

import com.leron.api.model.DTO.salary.SalaryRequest;
import com.leron.api.model.DTO.salary.SalaryResponse;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataRequest;
import com.leron.api.responses.DataResponse;
import com.leron.api.service.salary.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/salary")
public class SalaryController {

    @Autowired
    SalaryService salaryService;

    @GetMapping("/{userAuthId}")
    public DataListResponse<SalaryResponse> list(@PathVariable(value = "userAuthId", required = true) Long userAuthId){
        DataListResponse<SalaryResponse> list = salaryService.list(userAuthId);
        return list;
    }

    @PostMapping(
            value = "",
            consumes = "application/json",
            produces = "application/json"
    )
    public DataResponse<SalaryResponse> create(
            @RequestBody SalaryRequest requestCreation,
            @RequestHeader(name = "locale", required = true) String locale,
            @RequestHeader(name = "Authorization", required = true) String authorization
    ) {

        DataRequest<SalaryRequest> request = new DataRequest<>(requestCreation, locale, authorization);
        DataResponse<SalaryResponse> response = new DataResponse<>();

        try {
            response = salaryService.create(request);
            return response;

        } catch (ApplicationBusinessException error){
            response.setResponse(error);
        }
        return response;
    }

}
