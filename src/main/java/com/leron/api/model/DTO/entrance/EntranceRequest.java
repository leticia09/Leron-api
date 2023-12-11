package com.leron.api.model.DTO.entrance;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Component
@Data
public class EntranceRequest {
    private String source;
    private String type;
    private Long ownerId;
    private BigDecimal salary;
    private Long bankId;
    private String accountNumber;
    private String frequency;
    private String initialDate;
    private String finalDate;
    private Long monthReceive;
    private Long dayReceive;
    private Long userAuthId;
    private Long index;
}
