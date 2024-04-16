package com.leron.api.controller;

import com.leron.api.model.DTO.financialEntity.FinancialEntityRequest;
import com.leron.api.model.DTO.financialEntity.FinancialEntityResponse;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataResponse;
import com.leron.api.service.financialEntity.FinancialEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/financial-entity")
public class FinancialEntityController {

    @Autowired
    FinancialEntityService service;

    @PostMapping
    public DataResponse<FinancialEntityResponse> create(@RequestBody FinancialEntityRequest requestDTO,
                                                        @RequestHeader(name = "locale", required = false) String locale,
                                                        @RequestHeader(name = "Authorization", required = false) String authorization) throws ApplicationBusinessException {

        DataResponse<FinancialEntityResponse> response = new DataResponse<>();
        try {
            response = service.create(requestDTO);
        } catch (ApplicationBusinessException error) {
            response.setResponse(error);
        }
        return response;

    }

    @GetMapping("/{userAuthId}")
    public DataListResponse<FinancialEntityResponse> list(@PathVariable(value = "userAuthId", required = true) Long userAuthId) {
        return service.list(userAuthId);
    }

    @PatchMapping("")
    public  DataResponse<FinancialEntityResponse> edit(@RequestBody FinancialEntityResponse memberRequest) {
        DataResponse<FinancialEntityResponse> response = new DataResponse<>();
        try {
            response = service.edit(memberRequest);
        } catch (ApplicationBusinessException error){
            response.setResponse(error);
        }

        return response;
    }

    @DeleteMapping("/{financialEntity}")
    public DataResponse<FinancialEntityResponse> delete(@PathVariable Long financialEntity)  {
        return service.delete(financialEntity);
    }
}
