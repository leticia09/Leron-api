package com.leron.api.model.DTO.registerBank;

import com.leron.api.model.entities.MemberEntity;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class CardRequest {
    private String name;
    private String owner;
    private Long finalNumber;
    private String modality;
    private Integer closingDate;
    private Integer dueDate;
}
