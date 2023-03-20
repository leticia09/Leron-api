package com.leron.api.model.DTO.card;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class CardDTO {
    private Long id;
    private Long userId;
    private String status;
    private String modality;
    private Long finalCard;
    private Long billClose;
    private Long bankId;
    private Long dueDate;
    private String bankName;
    private String userName;
    private String nickName;
}
