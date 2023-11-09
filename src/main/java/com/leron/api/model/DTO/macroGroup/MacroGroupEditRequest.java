package com.leron.api.model.DTO.macroGroup;

import com.leron.api.model.entities.SpecificGroup;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
public class MacroGroupEditRequest {
    private Long id;
    private String name;
    private Long userAuthId;
    private List<SpecificGroup> specificGroups;
    private String status;
    private int specificNumbers;
}
