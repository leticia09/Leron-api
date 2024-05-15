package com.leron.api.controller;

import com.leron.api.model.DTO.bankMovement.BankMovementResponse;
import com.leron.api.model.DTO.bankMovement.PaymentRequest;
import com.leron.api.model.DTO.bankMovement.ReceiveRequest;
import com.leron.api.model.DTO.bankMovement.TransferBankRequest;
import com.leron.api.model.DTO.graphic.GraphicResponse;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataResponse;
import com.leron.api.service.movementBank.MovementBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/movement-bank")
public class MovementBankController {

    @Autowired
    MovementBankService movementBankService;

    @GetMapping("data/{userAuthId}")
    public DataResponse<GraphicResponse> getProgramsData(@PathVariable(value = "userAuthId", required = true) Long userAuthId) {
        return movementBankService.getData(userAuthId);
    }

    @PostMapping("receive/{userAuthId}")
    public DataResponse<BankMovementResponse> createReceive(@RequestBody List<ReceiveRequest> requestDTO,
                                                            @PathVariable(value = "userAuthId", required = true) Long userAuthId) throws ApplicationBusinessException {

        DataResponse<BankMovementResponse> response = new DataResponse<>();
        try {
            response = movementBankService.createReceive(requestDTO, userAuthId);
        } catch (ApplicationBusinessException error) {
            response.setResponse(error);
        }
        return response;

    }
    @PostMapping("payment/{userAuthId}")
    public DataResponse<BankMovementResponse> createPayment(@RequestBody List<PaymentRequest> requestDTO,
                                                            @PathVariable(value = "userAuthId", required = true) Long userAuthId) throws ApplicationBusinessException {

        DataResponse<BankMovementResponse> response = new DataResponse<>();
        try {
            response = movementBankService.createPayment(requestDTO, userAuthId);
        } catch (ApplicationBusinessException error) {
            response.setResponse(error);
        }
        return response;

    }
    @PostMapping("transfer/{userAuthId}")
    public DataResponse<BankMovementResponse> createTransfer(@RequestBody List<TransferBankRequest> requestDTO,
                                                            @PathVariable(value = "userAuthId", required = true) Long userAuthId) throws ApplicationBusinessException {

        DataResponse<BankMovementResponse> response = new DataResponse<>();
        try {
            response = movementBankService.createTransfer(requestDTO, userAuthId);
        } catch (ApplicationBusinessException error) {
            response.setResponse(error);
        }
        return response;

    }

    @GetMapping("/{userAuthId}")
    public DataListResponse<BankMovementResponse> list(@PathVariable(value = "userAuthId", required = true) Long userAuthId){
        return movementBankService.list(userAuthId);
    }

}
