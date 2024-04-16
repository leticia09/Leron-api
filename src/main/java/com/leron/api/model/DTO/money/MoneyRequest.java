package com.leron.api.model.DTO.money;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class MoneyRequest {
    private Long userAuthId;
    private String currency;
    private String value;
    private Long ownerId;
}
