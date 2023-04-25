package com.leron.api.validator.user;

import com.leron.api.model.DTO.user.UserRequest;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataRequest;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {
    public static void validatorUser(DataRequest<UserRequest> userRequest) throws ApplicationBusinessException {
        if(userRequest.getData().getCpf() == null){
            throw new ApplicationBusinessException("ERROR", "CPF_IS_EMPTY");
        }
        if(userRequest.getData().getEmail() == null){
            throw new ApplicationBusinessException("ERROR", "EMAIL_IS_EMPTY");
        }
        if(userRequest.getData().getName() == null){
            throw new ApplicationBusinessException("ERROR", "NOME_IS_EMPTY");
        }
        if(userRequest.getData().getPermissao() == null){
            throw new ApplicationBusinessException("ERROR", "PERMISSAO_IS_EMPTY");
        }

    }
}
