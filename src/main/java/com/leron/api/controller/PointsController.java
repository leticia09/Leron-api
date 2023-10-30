package com.leron.api.controller;

import com.leron.api.model.DTO.graphic.GraphicResponse;
import com.leron.api.model.DTO.points.PointsRequest;
import com.leron.api.model.DTO.points.PointsResponse;
import com.leron.api.model.DTO.points.TransferRequest;
import com.leron.api.model.DTO.points.TypeScoreDTO;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataRequest;
import com.leron.api.responses.DataResponse;
import com.leron.api.service.points.PointsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/points")
public class PointsController {
    @Autowired
    PointsService pointsService;

    @GetMapping("/{userAuthId}")
    public DataListResponse<PointsResponse> list(@PathVariable(value = "userAuthId", required = true) Long userAuthId){
        return pointsService.list(userAuthId);
    }

    @PostMapping(
            value = "",
            consumes = "application/json",
            produces = "application/json"
    )
    public DataResponse<PointsResponse> create(
            @RequestBody List<PointsRequest> requestCreation,
            @RequestHeader(name = "locale", required = true) String locale,
            @RequestHeader(name = "Authorization", required = true) String authorization
    ) {

        DataRequest<List<PointsRequest>> request = new DataRequest<>(requestCreation, locale, authorization);
        DataResponse<PointsResponse> response = new DataResponse<>();

        try {
            response = pointsService.create(request);
            return response;

        } catch (ApplicationBusinessException error){
            response.setResponse(error);
        }
        return response;
    }

    @GetMapping("type-of-score")
    public DataResponse<List<TypeScoreDTO>> getTypeOfScore() {
        return pointsService.getType();
    }

    @GetMapping("programs/{userAuthId}")
    public DataResponse<List<TypeScoreDTO>> getProgramsById(@PathVariable(value = "userAuthId", required = true) Long userAuthId){
        return pointsService.getProgramsById(userAuthId);
    }

    @PostMapping(
            value = "transfer",
            consumes = "application/json",
            produces = "application/json"
    )
    public DataResponse<TransferRequest> transfer(
            @RequestBody TransferRequest requestCreation,
            @RequestHeader(name = "locale", required = true) String locale,
            @RequestHeader(name = "Authorization", required = true) String authorization
    ) {

        DataRequest<TransferRequest> request = new DataRequest<>(requestCreation, locale, authorization);
        DataResponse<TransferRequest> response = new DataResponse<>();

        try {
            response = pointsService.transfer(request);
            return response;

        } catch (ApplicationBusinessException error){
            response.setResponse(error);
        }
        return response;
    }

    @GetMapping("programs-data/{userAuthId}")
    public DataResponse<GraphicResponse> getProgramsData(@PathVariable(value = "userAuthId", required = true) Long userAuthId){
        return pointsService.getProgramsData(userAuthId);
    }

}
