package com.leron.api.model.DTO.bankMovement;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class TransferBankRequest {
    private Long id;
    private Long userAuthId;
    private Long index;
    private Long bankOriginId;
    private Long bankDestinyId;
    private Long ownerOriginId;
    private Long ownerDestinyId;
    private Long accountOriginId;
    private Long accountDestinyId;
    private String receiver;
    private String value;
    private String dateTransfer;
    private String obs;
}
