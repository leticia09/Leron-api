package com.leron.api.model.DTO.registerBank;

import com.leron.api.model.entities.Member;
import com.leron.api.model.entities.Score;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Data
public class CardResponse {
    private Long id;
    private String name;
    private Member owner;
    private String status;
    private Long finalNumber;
    private String modality;
    private Integer closingDate;
    private Integer dueDate;
    private BigDecimal point;
    private String currency;
    private Score program;
}
