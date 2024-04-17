package com.leron.api.controller;

import com.leron.api.model.DTO.Goals.GoalsManagementResponse;
import com.leron.api.model.DTO.Goals.GoalsRequest;
import com.leron.api.model.DTO.Goals.GoalsResponse;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataResponse;
import com.leron.api.service.Goals.GoalsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/goals")
public class GoalsController {
    @Autowired
    private GoalsService service;

    @PostMapping
    public DataResponse<GoalsResponse> create(@RequestBody List<GoalsRequest> requestDTO,
                                              @RequestHeader(name = "locale", required = false) String locale,
                                              @RequestHeader(name = "Authorization", required = false) String authorization) throws ApplicationBusinessException {

        DataResponse<GoalsResponse> response = new DataResponse<>();
        try {
            response = service.create(requestDTO, locale, authorization);
        } catch (ApplicationBusinessException error) {
            response.setResponse(error);
        }
        return response;

    }

    @GetMapping("/{userAuthId}/{month}/{year}/{owners}")
    public DataResponse<GoalsManagementResponse> getManagementData(@PathVariable(value = "userAuthId", required = true) Long userAuthId,
                                                                   @PathVariable(value = "month", required = true) int month,
                                                                   @PathVariable(value = "year", required = true) int year,
                                                                   @PathVariable(value = "owners", required = true) List<Long> owners) {
        return service.getManagementData(userAuthId, month, year, owners);
    }
}
