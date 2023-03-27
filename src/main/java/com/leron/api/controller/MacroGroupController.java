package com.leron.api.controller;

import com.leron.api.model.DTO.macroGroup.MacroGroupRequest;
import com.leron.api.model.DTO.macroGroup.MacroGroupResponse;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataRequest;
import com.leron.api.responses.DataResponse;
import com.leron.api.service.macroGroup.MacroGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/macro-group")
public class MacroGroupController {

    @Autowired
    MacroGroupService macroGroupService;

    @GetMapping("/{userAuthId}")
    public DataListResponse<MacroGroupResponse> list(@PathVariable(value = "userAuthId", required = true) Long userAuthId){
        DataListResponse<MacroGroupResponse> list = macroGroupService.list(userAuthId);
        return list;
    }

    @PostMapping(
            value = "",
            consumes = "application/json",
            produces = "application/json"
    )
    public DataResponse<MacroGroupResponse> create(
            @RequestBody MacroGroupRequest requestCreation,
            @RequestHeader(name = "locale", required = true) String locale,
            @RequestHeader(name = "Authorization", required = true) String authorization
    ) {

        DataRequest<MacroGroupRequest> request = new DataRequest<>(requestCreation, locale, authorization);
        DataResponse<MacroGroupResponse> response = new DataResponse<>();

        try {
            response = macroGroupService.create(request);
            return response;

        } catch (ApplicationBusinessException error){
            response.setResponse(error);
        }
        return response;
    }
}
