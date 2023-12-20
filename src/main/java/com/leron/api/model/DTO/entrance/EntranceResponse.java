package com.leron.api.model.DTO.entrance;

import com.leron.api.model.entities.Bank;
import com.leron.api.model.entities.Member;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Component
@Data
public class EntranceResponse {
    private Long id;
    private String source;
    private String type;
    private Member owner;
    private BigDecimal salary;
    private String bankName;
    private String accountNumber;
    private String frequency;
    private Timestamp initialDate;
    private Timestamp finalDate;
    private Long monthReceive;
    private Long dayReceive;
    private String status;
    private String currency;
    private BigDecimal valueReceived;
    private String financialCardName;
    private String money;
}
