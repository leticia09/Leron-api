package com.leron.api.controller;

import com.leron.api.model.UserDTO;
import com.leron.api.model.UserRequest;
import com.leron.api.model.UserResponse;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataRequest;
import com.leron.api.responses.DataResponse;
import com.leron.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("")
    public DataListResponse<UserDTO> list(){
        DataListResponse<UserDTO> list = userService.getUser();
        return list;
    }

    @PostMapping(
            value = "",
            consumes = "application/json",
            produces = "application/json"
    )
    public DataResponse<UserResponse> create(
            @RequestBody UserRequest requestCreation,
            @RequestHeader(name = "locale", required = true) String locale,
            @RequestHeader(name = "Authorization", required = true) String authorization
         ) {

        DataRequest<UserRequest> request = new DataRequest<>(requestCreation, locale, authorization);
        DataResponse<UserResponse> response = new DataResponse<>();

        try {
            response = userService.create(request, locale, authorization);
            return response;

        } catch (ApplicationBusinessException error){
            response.setResponse(error);
        }
        return response;
    }

}
