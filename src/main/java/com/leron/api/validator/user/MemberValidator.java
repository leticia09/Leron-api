package com.leron.api.validator.user;

import com.leron.api.model.DTO.user.UserRequest;
import com.leron.api.responses.ApplicationBusinessException;
import org.springframework.stereotype.Component;

@Component
public class MemberValidator {
    public static void validateUser(UserRequest userRequest) throws ApplicationBusinessException {
        if (userRequest.getCpf() == null || userRequest.getCpf().isEmpty()) {
            throw new ApplicationBusinessException("ERROR", "CPF_IS_EMPTY");
        }
        if (userRequest.getEmail() == null || userRequest.getEmail().isEmpty()) {
            throw new ApplicationBusinessException("ERROR", "EMAIL_IS_EMPTY");
        }
        if (userRequest.getName() == null || userRequest.getName().isEmpty()) {
            throw new ApplicationBusinessException("ERROR", "NOME_IS_EMPTY");
        }
        if (userRequest.getPermission() == null || userRequest.getPermission().isEmpty()) {
            throw new ApplicationBusinessException("ERROR", "PERMISSAO_IS_EMPTY");
        }
    }
}
