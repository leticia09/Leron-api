package com.leron.api.controller;

import com.leron.api.model.DTO.userAuth.UserAuthRequest;
import com.leron.api.model.DTO.userAuth.UserAuthRespose;

import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataRequest;
import com.leron.api.responses.DataResponse;
import com.leron.api.service.userAuth.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/user-auth")
public class UserAuthController {

    @Autowired
    UserAuthService userAuthService;

    @GetMapping("")
    public DataListResponse<UserAuthRespose> list(){
        DataListResponse<UserAuthRespose> list = userAuthService.getUser();
        return list;
    }

    @PostMapping(
            value = "",
            consumes = "application/json",
            produces = "application/json"
    )
    public DataResponse<UserAuthRespose> create(
            @RequestBody UserAuthRequest requestCreation,
            @RequestHeader(name = "locale", required = true) String locale,
            @RequestHeader(name = "Authorization", required = true) String authorization
    ) {

        DataRequest<UserAuthRequest> request = new DataRequest<>(requestCreation, locale, authorization);
        DataResponse<UserAuthRespose> response = new DataResponse<>();

        try {
            response = userAuthService.create(request);
            return response;

        } catch (ApplicationBusinessException error){
            response.setResponse(error);
        }
        return response;
    }

    @PostMapping("/validate")
    public List<String> validate (
            @RequestBody UserAuthRequest requestCreation
    ){
        return userAuthService.validate(requestCreation);
    }


}
