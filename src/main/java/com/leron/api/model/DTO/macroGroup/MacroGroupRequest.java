package com.leron.api.model.DTO.macroGroup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MacroGroupRequest {
    private String name;
    private Long userAuthId;
}
