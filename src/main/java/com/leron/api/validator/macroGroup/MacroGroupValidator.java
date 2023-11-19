package com.leron.api.validator.macroGroup;

import com.leron.api.model.DTO.macroGroup.MacroGroupEditRequest;
import com.leron.api.model.DTO.macroGroup.MacroGroupRequest;
import com.leron.api.model.entities.MacroGroup;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class MacroGroupValidator {
    public static void validatorMacroGroup(DataRequest<MacroGroupRequest> request, List<MacroGroup> current) throws ApplicationBusinessException {
        AtomicReference<Boolean> macroGroupNameExists = new AtomicReference<>(false);
        AtomicReference<Boolean> specificGroupNameExists = new AtomicReference<>(false);

        current.forEach(macroCurrent -> {
            if(request.getData().getName().equalsIgnoreCase(macroCurrent.getName())) {
                macroGroupNameExists.set(true);
            }

//            if(request.getData().getSpecificGroups() != null) {
//                macroCurrent.getSpecificGroups().forEach(specificGroup -> {
//                    if (request.getData().getSpecificGroups().stream().anyMatch(specificGroup1 -> specificGroup.getName().equals(specificGroup1.getName()))) {
//                        specificGroupNameExists.set(true);
//                    }
//                });
//            }

        });

        if(request.getData().getName() == null || request.getData().getName().isEmpty()){
            throw new ApplicationBusinessException("ERROR", "NAME_IS_EMPTY");
        }

        if(macroGroupNameExists.get()) {
            throw new ApplicationBusinessException("ERROR", "MACRO_GROUP_NAME_ALREADY_EXISTS");
        }

        if(specificGroupNameExists.get()) {
            throw new ApplicationBusinessException("ERROR", "SPECIFIC_GROUP_NAME_ALREADY_EXISTS");
        }

    }
    public static void validatorMacroGroupEdit(DataRequest<MacroGroupEditRequest> request, List<MacroGroup> current) throws ApplicationBusinessException {
        AtomicReference<Boolean> macroGroupNameExists = new AtomicReference<>(false);
        AtomicReference<Boolean> specificGroupNameExists = new AtomicReference<>(false);

        current.forEach(macroCurrent -> {
            if(request.getData().getName().equalsIgnoreCase(macroCurrent.getName())) {
                macroGroupNameExists.set(true);
            }

            macroCurrent.getSpecificGroups().forEach(specificGroup -> {
                if (request.getData().getSpecificGroups().stream().anyMatch(specificGroup1 -> specificGroup.getName().equals(specificGroup1.getName()))) {
                    specificGroupNameExists.set(true);
                }
            });
        });

        if(request.getData().getName() == null || request.getData().getName().isEmpty()){
            throw new ApplicationBusinessException("ERROR", "NAME_IS_EMPTY");
        }

//        if(macroGroupNameExists.get()) {
//            throw new ApplicationBusinessException("ERROR", "MACRO_GROUP_NAME_ALREADY_EXISTS");
//        }

//        if(specificGroupNameExists.get()) {
//            throw new ApplicationBusinessException("ERROR", "SPECIFIC_GROUP_NAME_ALREADY_EXISTS");
//        }

    }
}
