package com.leron.api.controller;

import com.leron.api.model.DTO.money.MoneyRequest;
import com.leron.api.model.DTO.money.MoneyResponse;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataResponse;
import com.leron.api.service.money.MoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/money")
public class MoneyController {

    @Autowired
    MoneyService service;

    @PostMapping
    public DataResponse<MoneyResponse> create(@RequestBody List<MoneyRequest> requestDTO,
                                              @RequestHeader(name = "locale", required = false) String locale,
                                              @RequestHeader(name = "Authorization", required = false) String authorization) throws ApplicationBusinessException {

        DataResponse<MoneyResponse> response = new DataResponse<>();
        try {
            response = service.create(requestDTO);
        } catch (ApplicationBusinessException error) {
            response.setResponse(error);
        }
        return response;

    }

    @GetMapping("/{userAuthId}")
    public DataListResponse<MoneyResponse> list(@PathVariable(value = "userAuthId", required = true) Long userAuthId) {
        return service.list(userAuthId);
    }
}
