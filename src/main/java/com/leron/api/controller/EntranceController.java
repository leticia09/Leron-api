package com.leron.api.controller;

import com.leron.api.model.DTO.entrance.EntranceRequest;
import com.leron.api.model.DTO.entrance.EntranceResponse;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataResponse;

import com.leron.api.service.entrance.EntranceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/entrance")
public class EntranceController {
    @Autowired
    private EntranceService service;

    @PostMapping
    public DataResponse<EntranceResponse> createBank(@RequestBody EntranceRequest requestDTO,
                                                         @RequestHeader(name = "locale", required = false) String locale,
                                                         @RequestHeader(name = "Authorization", required = false) String authorization) throws ApplicationBusinessException {

        DataResponse<EntranceResponse> response = new DataResponse<>();
        response = service.create(requestDTO, locale, authorization);
        return response;

    }
}
