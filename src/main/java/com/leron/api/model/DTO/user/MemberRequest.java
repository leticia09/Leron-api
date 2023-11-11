package com.leron.api.model.DTO.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberRequest {
    private String name;
    private Long userAuthId;
    private Long index;
    private String color;
}
