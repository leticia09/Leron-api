package com.leron.api.model.DTO.card;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CardResponse {
    private Long id;
    private String userId;
    private String status;
    private String modality;
    private Long finalCard;
    private Long bankId;
    private Long dueDate;
}
