package com.leron.api.mapper.macroGroup;

import com.leron.api.model.DTO.macroGroup.MacroGroupRequest;
import com.leron.api.model.DTO.macroGroup.MacroGroupResponse;
import com.leron.api.model.entities.MacroGroupEntity;
import com.leron.api.responses.DataListResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class MacroGroupMapper {
    public static DataListResponse<MacroGroupResponse> macroGroupEntitiesToDataListResponse(List<MacroGroupEntity> macroGroupEntityList){

        DataListResponse<MacroGroupResponse> response = new DataListResponse<>();
        List<MacroGroupResponse> responseList = new ArrayList<>();

        for (MacroGroupEntity macro : macroGroupEntityList) {
            MacroGroupResponse macroResponse = new MacroGroupResponse();

            macroResponse.setId(macro.getId());
            macroResponse.setName(macro.getName());

            responseList.add(macroResponse);
        }
        response.setData(responseList);

        return response;
    }

    public static MacroGroupEntity createMacroGroupFromMacroGroupRequest(MacroGroupRequest macroGroupRequest) {

        MacroGroupEntity macro = new MacroGroupEntity();

        macro.setName(macroGroupRequest.getName());
        macro.setCreatedIn(new Date());

        return macro;
    }

    public static MacroGroupResponse createSalaryResponse  (MacroGroupEntity entity) {

        MacroGroupResponse response = new MacroGroupResponse();

        response.setId(entity.getId());
        response.setName(entity.getName());

        return response;
    }
}
