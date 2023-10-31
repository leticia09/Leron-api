package com.leron.api.model.DTO.points;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class StatusRequest {
    private Long programId;
    private String status;
    private Long userAuthId;

}
