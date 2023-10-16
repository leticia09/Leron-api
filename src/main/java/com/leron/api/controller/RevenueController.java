package com.leron.api.controller;

import com.leron.api.model.DTO.revenue.RevenueRequest;
import com.leron.api.model.DTO.revenue.RevenueResponse;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataRequest;
import com.leron.api.responses.DataResponse;
import com.leron.api.service.revenue.RevenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/revenue")
public class RevenueController {

    @Autowired
    RevenueService revenueService;

    @GetMapping("/{userAuthId}")
    public DataListResponse<RevenueResponse> list(@PathVariable(value = "userAuthId", required = true) Long userAuthId){
        //DataListResponse<RevenueResponse> list = revenueService.list(userAuthId);
        return null;
    }

    @PostMapping(
            value = "",
            consumes = "application/json",
            produces = "application/json"
    )
    public DataResponse<RevenueResponse> create(
            @RequestBody RevenueRequest requestCreation,
            @RequestHeader(name = "locale", required = true) String locale,
            @RequestHeader(name = "Authorization", required = true) String authorization
    ) {

        DataRequest<RevenueRequest> request = new DataRequest<>(requestCreation, locale, authorization);
        DataResponse<RevenueResponse> response = new DataResponse<>();

        try {
            response = revenueService.create(request);
            return response;

        } catch (ApplicationBusinessException error){
            response.setResponse(error);
        }
        return response;
    }
}
