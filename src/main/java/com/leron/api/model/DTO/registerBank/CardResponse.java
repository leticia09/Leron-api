package com.leron.api.model.DTO.registerBank;

import com.leron.api.model.entities.MemberEntity;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class CardResponse {
    private Long id;
    private String name;
    private MemberEntity owner;
    private String status;
    private Long finalNumber;
    private String modality;
    private Integer closingDate;
    private Integer dueDate;
}
