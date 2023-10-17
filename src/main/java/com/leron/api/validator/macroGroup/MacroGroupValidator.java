package com.leron.api.validator.macroGroup;

import com.leron.api.model.DTO.macroGroup.MacroGroupRequest;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataRequest;
import org.springframework.stereotype.Component;

@Component
public class MacroGroupValidator {
    public static void validatorMacroGroup(DataRequest<MacroGroupRequest> request) throws ApplicationBusinessException {

        if(request.getData().getName() == null || request.getData().getName().isEmpty()){
            throw new ApplicationBusinessException("Lascou", "NAME_IS_EMPTY");
        }

    }
}
