package com.leron.api.controller;

import com.leron.api.model.DTO.card.CardRequest;
import com.leron.api.model.DTO.card.CardResponse;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataRequest;
import com.leron.api.responses.DataResponse;
import com.leron.api.service.card.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/card")
public class CardController {

    @Autowired
    CardService cardService;

    @GetMapping("/{userAuthId}")
    public DataListResponse<CardResponse> list(@PathVariable(value = "userAuthId", required = true) Long userAuthId){
        DataListResponse<CardResponse> list = cardService.list(userAuthId);
        return list;
    }

    @PostMapping(
            value = "",
            consumes = "application/json",
            produces = "application/json"
    )
    public DataResponse<CardResponse> create(
            @RequestBody CardRequest requestCreation,
            @RequestHeader(name = "locale", required = true) String locale,
            @RequestHeader(name = "Authorization", required = true) String authorization
    ) {

        DataRequest<CardRequest> request = new DataRequest<>(requestCreation, locale, authorization);
        DataResponse<CardResponse> response = new DataResponse<>();

        try {
            response = cardService.create(request);
            return response;

        } catch (ApplicationBusinessException error){
            response.setResponse(error);
        }
        return response;
    }
}
