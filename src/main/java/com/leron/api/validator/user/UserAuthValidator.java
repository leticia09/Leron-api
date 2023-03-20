package com.leron.api.validator.user;

import com.leron.api.model.DTO.userAuth.UserAuthRequest;
import com.leron.api.model.entities.UserAuthEntity;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class UserAuthValidator {
    public static void validatorUser(DataRequest<UserAuthRequest> userRequest, List<UserAuthEntity> userList) throws ApplicationBusinessException {

        if(userRequest.getData().getPassword() == null){
            throw new ApplicationBusinessException("400", "PASSWORD_IS_EMPTY");
        }

        AtomicBoolean exists = new AtomicBoolean(false);

        if(!userList.isEmpty()) {
            userList.forEach(currentUser -> {
               if(currentUser.getLogin().equals(userRequest.getData().getLogin())){
                   exists.set(true);
               }
            });
        }

        if(exists.get()){
            throw new ApplicationBusinessException("400", "USER_EXISTS");
        }

    }
}
