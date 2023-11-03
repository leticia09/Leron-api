package com.leron.api.service.macroGroup;

import com.leron.api.mapper.macroGroup.MacroGroupMapper;
import com.leron.api.model.DTO.macroGroup.MacroGroupRequest;
import com.leron.api.model.DTO.macroGroup.MacroGroupResponse;
import com.leron.api.model.entities.MacroGroup;
import com.leron.api.model.entities.SpecificGroup;
import com.leron.api.repository.MacroGroupRepository;
import com.leron.api.repository.SpecificGroupRepository;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataRequest;
import com.leron.api.responses.DataResponse;
import com.leron.api.validator.macroGroup.MacroGroupValidator;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import java.util.List;

@Service
public class MacroGroupService {

    private final MacroGroupRepository macroGroupRepository;
    private final SpecificGroupRepository specificGroupRepository;

    public MacroGroupService(MacroGroupRepository macroGroupRepository, SpecificGroupRepository specificGroupRepository) {
        this.macroGroupRepository = macroGroupRepository;
        this.specificGroupRepository = specificGroupRepository;
    }

    public DataListResponse<MacroGroupResponse> list(Long userAuthId) {
        return MacroGroupMapper.macroGroupEntitiesToDataListResponse(macroGroupRepository.findAllGroupByUserAuthId(userAuthId));
    }

    public DataResponse<MacroGroupResponse> create(DataRequest<MacroGroupRequest> request) throws ApplicationBusinessException {
        DataResponse<MacroGroupResponse> response = new DataResponse<>();

        List<MacroGroup> macroGroupList = macroGroupRepository.findAllGroupByUserAuthId(request.getData().getUserAuthId());
        MacroGroupValidator.validatorMacroGroup(request, macroGroupList);

        MacroGroup macroGroup = MacroGroupMapper.createMacroGroupFromMacroGroupRequest(request.getData());

        saveMacroGroup(macroGroup);

        macroGroup.getSpecificGroups().forEach(specificGroup -> {
            specificGroup.setMacroGroup(macroGroup);
            specificGroup.setStatus("ACTIVE");
            saveSpecificGroup(specificGroup);
        });


        MacroGroupResponse userResponse = MacroGroupMapper.createSalaryResponse(macroGroup);
        response.setData(userResponse);
        response.setMessage("success");
        return response;
    }

    private void saveMacroGroup(MacroGroup macroGroup) {
        macroGroupRepository.save(macroGroup);
    }

    private void saveSpecificGroup(SpecificGroup specificGroup) {
        specificGroupRepository.save(specificGroup);
    }
}
