package com.leron.api.service.macroGroup;

import com.leron.api.mapper.macroGroup.MacroGroupMapper;
import com.leron.api.model.DTO.macroGroup.MacroGroupEditRequest;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class MacroGroupService {

    private final MacroGroupRepository macroGroupRepository;
    private final SpecificGroupRepository specificGroupRepository;

    public MacroGroupService(MacroGroupRepository macroGroupRepository, SpecificGroupRepository specificGroupRepository) {
        this.macroGroupRepository = macroGroupRepository;
        this.specificGroupRepository = specificGroupRepository;
    }

    public DataListResponse<MacroGroupResponse> list(Long userAuthId) {
        return MacroGroupMapper.macroGroupEntitiesToDataListResponse(macroGroupRepository.findAllByUserAuthIdAndDeletedFalseOrderByNameAsc(userAuthId));
    }

    public DataResponse<MacroGroupResponse> create(DataRequest<MacroGroupRequest> request) throws ApplicationBusinessException {
        DataResponse<MacroGroupResponse> response = new DataResponse<>();

        List<MacroGroup> macroGroupList = macroGroupRepository.findAllByUserAuthIdAndDeletedFalseOrderByNameAsc(request.getData().getUserAuthId());
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

    public DataResponse<MacroGroupResponse> edit(DataRequest<MacroGroupEditRequest> request) throws ApplicationBusinessException {
        DataResponse<MacroGroupResponse> response = new DataResponse<>();

        List<MacroGroup> macroGroupList = macroGroupRepository.findAllByUserAuthIdAndDeletedFalseOrderByNameAsc(request.getData().getUserAuthId());
        MacroGroupValidator.validatorMacroGroupEdit(request, macroGroupList);

        MacroGroup macroGroup = MacroGroupMapper.createMacroGroupFromMacroGroupEditRequest(request.getData());

        List<SpecificGroup> specificGroupList = new ArrayList<>();


        for (MacroGroup groups : macroGroupList) {
            if(groups.getId().equals(request.getData().getId())) {
                specificGroupList.addAll(groups.getSpecificGroups());
            }
        }

        for (SpecificGroup specificGroup : specificGroupList) {
            boolean existsInRequest = false;

            for (SpecificGroup group : request.getData().getSpecificGroups()) {
                if (Objects.equals(group.getId(), specificGroup.getId())) {
                    existsInRequest = true;
                    break;
                }
            }

            specificGroup.setDeleted(!existsInRequest);
        }

        request.getData().getSpecificGroups().stream()
                .filter(group -> specificGroupList.stream().noneMatch(specificGroup -> Objects.equals(specificGroup.getId(), group.getId())))
                .forEach(newSpecificGroup -> {
                    newSpecificGroup.setDeleted(false);
                    specificGroupList.add(newSpecificGroup);
                });


        macroGroup.setSpecificGroups(specificGroupList);
        saveMacroGroup(macroGroup);

        macroGroup.getSpecificGroups().forEach(specificGroup -> {
            specificGroup.setMacroGroup(macroGroup);
            specificGroup.setStatus(macroGroup.getStatus());
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
