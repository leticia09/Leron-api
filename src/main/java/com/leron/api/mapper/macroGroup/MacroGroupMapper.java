package com.leron.api.mapper.macroGroup;

import com.leron.api.model.DTO.macroGroup.MacroGroupEditRequest;
import com.leron.api.model.DTO.macroGroup.MacroGroupRequest;
import com.leron.api.model.DTO.macroGroup.MacroGroupResponse;
import com.leron.api.model.entities.MacroGroup;
import com.leron.api.responses.DataListResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
public class MacroGroupMapper {
    public static DataListResponse<MacroGroupResponse> macroGroupEntitiesToDataListResponse(List<MacroGroup> macroGroupEntityList) {

        DataListResponse<MacroGroupResponse> response = new DataListResponse<>();
        List<MacroGroupResponse> responseList = new ArrayList<>();

        for (MacroGroup macro : macroGroupEntityList) {
            MacroGroupResponse macroResponse = new MacroGroupResponse();

            macroResponse.setId(macro.getId());
            macroResponse.setName(macro.getName());
            macroResponse.setUserAuthId(macro.getUserAuthId());
            macroResponse.setSpecificGroups(macro.getSpecificGroups());
            macroResponse.setSpecificNumbers(macro.getSpecificGroups().size());
            macroResponse.setStatus(macro.getStatus());

            responseList.add(macroResponse);
        }
        response.setData(responseList);

        return response;
    }

    public static MacroGroup createMacroGroupFromMacroGroupRequest(MacroGroupRequest macroGroupRequest) {

        MacroGroup macro = new MacroGroup();

        macro.setName(macroGroupRequest.getName().substring(0, 1).toUpperCase() + macroGroupRequest.getName().substring(1).toLowerCase());
        macro.setUserAuthId(macroGroupRequest.getUserAuthId());
        macro.setSpecificGroups(macroGroupRequest.getSpecificGroups());
        macro.setStatus("ACTIVE");
        macro.setCreatedIn(new Date());
        macro.setDeleted(false);

        return macro;
    }

    public static MacroGroup createMacroGroupFromMacroGroupEditRequest(MacroGroupEditRequest macroGroupRequest) {

        MacroGroup macro = new MacroGroup();

        macro.setId(macroGroupRequest.getId());
        macro.setName(macroGroupRequest.getName().substring(0, 1).toUpperCase() + macroGroupRequest.getName().substring(1).toLowerCase());
        macro.setUserAuthId(macroGroupRequest.getUserAuthId());
        macro.setSpecificGroups(macroGroupRequest.getSpecificGroups());
        if(Objects.nonNull(macroGroupRequest.getStatus())) {
            if (macroGroupRequest.getStatus().equals("1")) {
                macro.setStatus("ACTIVE");
            }
            if (macroGroupRequest.getStatus().equals("2")) {
                macro.setStatus("INACTIVE");
            }
        }

        macro.setChangedIn(new Date());
        macro.setDeleted(false);

        return macro;
    }

    public static MacroGroupResponse createSalaryResponse(MacroGroup entity) {

        MacroGroupResponse response = new MacroGroupResponse();

        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setUserAuthId(entity.getUserAuthId());
        response.setSpecificGroups(entity.getSpecificGroups());

        return response;
    }
}
