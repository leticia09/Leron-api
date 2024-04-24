package com.leron.api.controller;

import com.leron.api.model.DTO.dashboard.DashboardManagementResponse;
import com.leron.api.responses.DataResponse;
import com.leron.api.service.dashboard.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    DashboardService service;

    @GetMapping("/{userAuthId}")
    public DataResponse<DashboardManagementResponse> list(@PathVariable(value = "userAuthId", required = true) Long userAuthId) {
        return service.getManagementData(userAuthId);
    }

}
