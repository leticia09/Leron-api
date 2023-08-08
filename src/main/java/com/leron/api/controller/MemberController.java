package com.leron.api.controller;

import com.leron.api.model.DTO.user.MemberRequest;
import com.leron.api.model.DTO.user.MemberResponse;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataRequest;
import com.leron.api.responses.DataResponse;
import com.leron.api.service.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/member")
public class MemberController {

    @Autowired
    MemberService userService;

    @GetMapping("/{userAuthId}")
    public DataListResponse<MemberResponse> list(@PathVariable(value = "userAuthId", required = true) Long userAuthId){
        DataListResponse<MemberResponse> list = userService.list(userAuthId);
        return list;
    }

    @PostMapping(
            value = "",
            consumes = "application/json",
            produces = "application/json"
    )
    public DataResponse<List<MemberResponse>> create(
            @RequestBody List<MemberRequest> requestCreation,
            @RequestHeader(name = "locale", required = true) String locale,
            @RequestHeader(name = "Authorization", required = true) String authorization
         ) {

        DataRequest<List<MemberRequest>> request = new DataRequest<>(requestCreation, locale, authorization);
        DataResponse<List<MemberResponse>> response = new DataResponse<>();

        try {
            response = userService.create(request, locale, authorization);
            return response;

        } catch (ApplicationBusinessException error){
            response.setResponse(error);
        }
        return response;
    }

}
