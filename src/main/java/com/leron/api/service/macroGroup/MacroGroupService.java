package com.leron.api.service.macroGroup;

import com.leron.api.mapper.macroGroup.MacroGroupMapper;
import com.leron.api.model.DTO.macroGroup.MacroGroupRequest;
import com.leron.api.model.DTO.macroGroup.MacroGroupResponse;
import com.leron.api.model.entities.MacroGroupEntity;
import com.leron.api.repository.MacroGroupRepository;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataRequest;
import com.leron.api.responses.DataResponse;
import com.leron.api.validator.macroGroup.MacroGroupValidator;
import org.springframework.stereotype.Service;

@Service
public class MacroGroupService {

    private final MacroGroupRepository macroGroupRepository;

    public MacroGroupService(MacroGroupRepository macroGroupRepository) {
        this.macroGroupRepository = macroGroupRepository;
    }

    public DataListResponse<MacroGroupResponse> list(Long userAuthId) {
        return MacroGroupMapper.macroGroupEntitiesToDataListResponse(macroGroupRepository.findAllByAuthUserId(userAuthId));
    }

    public DataResponse<MacroGroupResponse> create(DataRequest<MacroGroupRequest> request) throws ApplicationBusinessException {
        DataResponse<MacroGroupResponse> response = new DataResponse<>();
        MacroGroupValidator.validatorMacroGroup(request);

        MacroGroupEntity salary = MacroGroupMapper.createMacroGroupFromMacroGroupRequest(request.getData());
        macroGroupRepository.save(salary);
        MacroGroupResponse userResponse = MacroGroupMapper.createSalaryResponse(salary);
        response.setData(userResponse);
        response.setMessage("Sucesso");
        return response;
    }
}
