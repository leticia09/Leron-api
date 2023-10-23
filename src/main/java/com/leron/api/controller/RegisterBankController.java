package com.leron.api.controller;

import com.leron.api.model.DTO.registerBank.RegisterBankRequest;
import com.leron.api.model.DTO.registerBank.RegisterBankResponse;
import com.leron.api.model.entities.Bank;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataResponse;
import com.leron.api.service.registerBank.RegisterBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/register-bank")
public class RegisterBankController {
    @Autowired
    private RegisterBankService bankService;

    @PostMapping
    public DataResponse<RegisterBankResponse> createBank(@RequestBody RegisterBankRequest requestDTO,
                                                         @RequestHeader(name = "locale", required = false) String locale,
                                                         @RequestHeader(name = "Authorization", required = false) String authorization) throws ApplicationBusinessException {

        DataResponse<RegisterBankResponse> response = new DataResponse<>();
        try {
            response =  bankService.createBank(requestDTO, locale, authorization);
            return response;

        } catch (ApplicationBusinessException error){
            response.setResponse(error);
        }
        return response;

    }

    @GetMapping("/{userAuthId}")
    public DataListResponse<RegisterBankResponse> list(@PathVariable(value = "userAuthId", required = true) Long userAuthId){
        return bankService.list(userAuthId);
    }

    @GetMapping("/{userAuthId}/{id}")
    public DataResponse<RegisterBankResponse> getBankByIdAndUserAuthId(
            @PathVariable("userAuthId") Long userAuthId,
            @PathVariable("id") Long id
    ) {
        return bankService.getBankByIdAndUserAuthId(userAuthId, id);
    }
}
