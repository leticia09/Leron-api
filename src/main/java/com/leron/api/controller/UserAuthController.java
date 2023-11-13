package com.leron.api.controller;

import com.leron.api.model.DTO.userAuth.UserAuthRequest;
import com.leron.api.model.DTO.userAuth.UserAuthResponse;

import com.leron.api.model.DTO.userAuth.UserValidResponse;
import com.leron.api.model.DTO.userAuth.ValidResponse;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataRequest;
import com.leron.api.responses.DataResponse;
import com.leron.api.service.userAuth.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.Objects;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/user-auth")
public class UserAuthController {

    @Autowired
    UserAuthService userAuthService;

    @GetMapping("")
    public DataListResponse<UserAuthResponse> list(){
        DataListResponse<UserAuthResponse> list = userAuthService.getUser();
        return list;
    }

    @PostMapping(
            value = "",
            consumes = "application/json",
            produces = "application/json"
    )
    public DataResponse<UserAuthResponse> create(
            @RequestBody UserAuthRequest requestCreation,
            @RequestHeader(name = "locale", required = true) String locale,
            @RequestHeader(name = "Authorization", required = true) String authorization
    ) {

        DataRequest<UserAuthRequest> request = new DataRequest<>(requestCreation, locale, authorization);
        DataResponse<UserAuthResponse> response = new DataResponse<>();

        try {
            response = userAuthService.create(request);
            return response;

        } catch (ApplicationBusinessException error){
            response.setResponse(error);
        }
        return response;
    }

    @PostMapping("/validate")
    public DataResponse<ValidResponse> validate (
            @RequestBody UserAuthRequest requestCreation
    ) {
        DataResponse<ValidResponse> response = new DataResponse<>();
        UserValidResponse user =  userAuthService.validate(requestCreation);
        ValidResponse userAuthResponse = new ValidResponse();

        if(Objects.isNull(user)){
            userAuthResponse.setAuth(false);
            response.setData(userAuthResponse);
            response.setMessage("LOGIN_OR_PASSWORD_IS_WRONG");
        } else {
            userAuthResponse.setId(user.getId());
            userAuthResponse.setName(user.getName());
            userAuthResponse.setSex(user.getSex());
            userAuthResponse.setAuth(true);
            response.setData(userAuthResponse);
            response.setSeverity("success");
            response.setMessage("success");
        }

        return response;
    }


}
