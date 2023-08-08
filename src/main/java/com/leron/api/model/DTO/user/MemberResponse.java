package com.leron.api.model.DTO.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberResponse {
    private Long id;
    private String name;
    private Long userAuthId;
}
