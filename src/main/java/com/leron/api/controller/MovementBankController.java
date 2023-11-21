package com.leron.api.controller;

import com.leron.api.model.DTO.graphic.GraphicResponse;
import com.leron.api.responses.DataResponse;
import com.leron.api.service.movementBank.MovementBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/movement-bank")
public class MovementBankController {

    @Autowired
    MovementBankService movementBankService;

    @GetMapping("data/{userAuthId}")
    public DataResponse<GraphicResponse> getProgramsData(@PathVariable(value = "userAuthId", required = true) Long userAuthId){
        return movementBankService.getData(userAuthId);
    }

}
