package com.leron.api.controller;

import com.leron.api.model.DTO.points.PointsRequest;
import com.leron.api.model.DTO.points.PointsResponse;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataRequest;
import com.leron.api.responses.DataResponse;
import com.leron.api.service.points.PointsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/points")
public class PointsController {
    @Autowired
    PointsService pointsService;

    @GetMapping("/{userAuthId}")
    public DataListResponse<PointsResponse> list(@PathVariable(value = "userAuthId", required = true) Long userAuthId){
       // DataListResponse<PointsResponse> list = pointsService.list(userAuthId);
        return null;
    }

    @PostMapping(
            value = "",
            consumes = "application/json",
            produces = "application/json"
    )
    public DataResponse<PointsResponse> create(
            @RequestBody PointsRequest requestCreation,
            @RequestHeader(name = "locale", required = true) String locale,
            @RequestHeader(name = "Authorization", required = true) String authorization
    ) {

        DataRequest<PointsRequest> request = new DataRequest<>(requestCreation, locale, authorization);
        DataResponse<PointsResponse> response = new DataResponse<>();

        try {
            response = pointsService.create(request);
            return response;

        } catch (ApplicationBusinessException error){
            response.setResponse(error);
        }
        return response;
    }

}
