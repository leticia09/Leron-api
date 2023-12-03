package com.leron.api.controller;

import com.leron.api.model.DTO.registerBank.*;
import com.leron.api.model.entities.Bank;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataRequest;
import com.leron.api.responses.DataResponse;
import com.leron.api.service.dolar.DollarService;
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
            response = bankService.createBank(requestDTO, locale, authorization);
            return response;

        } catch (ApplicationBusinessException error) {
            response.setResponse(error);
        }
        return response;

    }

    @GetMapping("/{userAuthId}")
    public DataListResponse<RegisterBankResponse> list(@PathVariable(value = "userAuthId", required = true) Long userAuthId) {
        return bankService.list(userAuthId);
    }

    @GetMapping("/{userAuthId}/{id}")
    public DataResponse<RegisterBankResponse> getBankByIdAndUserAuthId(
            @PathVariable("userAuthId") Long userAuthId,
            @PathVariable("id") Long id
    ) {
        return bankService.getBankByIdAndUserAuthId(userAuthId, id);
    }

    @PutMapping("/{userAuthId}/cards/{cardId}")
    public DataResponse<CardResponse> updateCard(@PathVariable("userAuthId") Long userAuthId,
                                                 @PathVariable("cardId") Long cardId,
                                                 @RequestBody CardRequest cardRequest) {
        DataResponse<CardResponse> response = new DataResponse<>();

        try {
            return bankService.updateCard(userAuthId, cardId, cardRequest);
        } catch (ApplicationBusinessException error) {
            response.setResponse(error);
        }
        return response;
    }

    @DeleteMapping("/{bankId}")
    public DataResponse<RegisterBankResponse> delete(@PathVariable Long bankId) {
        return bankService.delete(bankId);
    }

    @DeleteMapping("account/{accountId}")
    public DataResponse<RegisterBankResponse> deleteAccount(@PathVariable Long accountId) {
        return bankService.deleteAccount(accountId);
    }

    @DeleteMapping("card/{cardId}")
    public DataResponse<RegisterBankResponse> deleteCard(@PathVariable Long cardId) {
        return bankService.deleteCard(cardId);
    }

    @PatchMapping(
            value = "/bank",
            consumes = "application/json",
            produces = "application/json"
    )
    public DataResponse<RegisterBankResponse> editBank(
            @RequestBody Bank request,
            @RequestHeader(name = "locale", required = true) String locale,
            @RequestHeader(name = "Authorization", required = true) String authorization
    ) {
        DataRequest<Bank> requestData = new DataRequest<>(request);
        DataResponse<RegisterBankResponse> response = new DataResponse<>();
        try {
            response = bankService.editBank(requestData.getData());
            return response;

        } catch (ApplicationBusinessException error){
            response.setResponse(error);
        }
        return response;

    }

    @PatchMapping(
            value = "/account",
            consumes = "application/json",
            produces = "application/json"
    )
    public DataResponse<AccountResponse> editAccount(
            @RequestBody AccountResponse request,
            @RequestHeader(name = "locale", required = true) String locale,
            @RequestHeader(name = "Authorization", required = true) String authorization
    ) {
        DataRequest<AccountResponse> requestData = new DataRequest<>(request);
        DataResponse<AccountResponse> response = new DataResponse<>();
        try {
            response = bankService.editAccount(requestData.getData());
            return response;

        } catch (ApplicationBusinessException error){
            response.setResponse(error);
        }
        return response;

    }

    @PatchMapping(
            value = "/card",
            consumes = "application/json",
            produces = "application/json"
    )
    public DataResponse<CardResponse> editAccount(
            @RequestBody CardResponse request,
            @RequestHeader(name = "locale", required = true) String locale,
            @RequestHeader(name = "Authorization", required = true) String authorization
    ) {
        DataRequest<CardResponse> requestData = new DataRequest<>(request);
        DataResponse<CardResponse> response = new DataResponse<>();
        try {
            response = bankService.editCard(requestData.getData());
            return response;

        } catch (ApplicationBusinessException error){
            response.setResponse(error);
        }
        return response;

    }
    

}
