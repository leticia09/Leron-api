package com.leron.api.model.DTO.bankMovement;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class ReceiveRequest {
    private String entrance;
    private String salary;
    private String receiveDate;
    private String referencePeriod;
    private Long  ownerId;
    private String obs;
    private Long bankId;
    private Long accountId;
    private Long moneyId;
    private String value;
}
