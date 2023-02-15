package com.leron.api.validator.user;

import com.leron.api.model.UserRequest;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataRequest;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {
    public static void validatorUser(DataRequest<UserRequest> userRequest) throws ApplicationBusinessException {
        if(userRequest.getData().getCpf() == null){
            throw new ApplicationBusinessException("Lascou", "CPF");
        }
        if(userRequest.getData().getEmail() == null){
            throw new ApplicationBusinessException("Lascou", "EMAIL");
        }
        if(userRequest.getData().getName() == null){
            throw new ApplicationBusinessException("Lascou", "NOME");
        }
        if(userRequest.getData().getPermissao() == null){
            throw new ApplicationBusinessException("Lascou", "PERMISSAO");
        }

    }
}
