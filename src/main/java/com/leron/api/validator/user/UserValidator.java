package com.leron.api.validator.user;

import com.leron.api.model.DTO.user.UserRequest;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataRequest;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {
    public static void validatorUser(DataRequest<UserRequest> userRequest) throws ApplicationBusinessException {
        if(userRequest.getData().getCpf() == null || userRequest.getData().getCpf().isEmpty()){
            throw new ApplicationBusinessException("ERROR", "CPF_IS_EMPTY");
        }
        if(userRequest.getData().getEmail() == null || userRequest.getData().getEmail().isEmpty()){
            throw new ApplicationBusinessException("ERROR", "EMAIL_IS_EMPTY");
        }
        if(userRequest.getData().getName() == null || userRequest.getData().getName().isEmpty()){
            throw new ApplicationBusinessException("ERROR", "NOME_IS_EMPTY");
        }
        if(userRequest.getData().getPermissao() == null || userRequest.getData().getPermissao().isEmpty()){
            throw new ApplicationBusinessException("ERROR", "PERMISSAO_IS_EMPTY");
        }

    }
}
