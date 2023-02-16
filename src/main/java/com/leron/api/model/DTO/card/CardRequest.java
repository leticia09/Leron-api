package com.leron.api.model.DTO.card;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class CardRequest {
    private String userId;
    private String status;
    private String modality;
    private String finalCard;
    private String bankId;
    private String dueDate;
    private String nickName;
}
