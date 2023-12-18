package com.leron.api.model.DTO.money;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class MoneyResponse {
    private Long id;
    private String currency;
    private String value;
    private Long ownerId;
}
