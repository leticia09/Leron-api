package com.leron.api.model.DTO.userAuth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidResponse {
    private Long id;
    private String name;
    private Long sex;
    private Boolean auth;

}
