package com.leron.api.validator.user;

import com.leron.api.model.DTO.user.MemberRequest;
import com.leron.api.responses.ApplicationBusinessException;
import org.springframework.stereotype.Component;

@Component
public class MemberValidator {
    public static void validateUser(MemberRequest userRequest) throws ApplicationBusinessException {
        if (userRequest.getName() == null || userRequest.getName().isEmpty()) {
            throw new ApplicationBusinessException("ERROR", "NOME_IS_EMPTY");
        }
    }
}
