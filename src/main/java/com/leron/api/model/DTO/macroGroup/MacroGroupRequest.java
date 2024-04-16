package com.leron.api.model.DTO.macroGroup;

import com.leron.api.model.entities.SpecificGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MacroGroupRequest {
    private String name;
    private Long userAuthId;
    private List<SpecificGroup> specificGroups;
    private Long index;
}
