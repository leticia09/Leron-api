package com.leron.api.model.DTO.entrance;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Data
public class EntranceRequest {
    private String source;
    private String type;
    private Long ownerId;
    private BigDecimal salary;
    private Long bankId;
    private String accountNumber;
    private Boolean paymentDone;
}
