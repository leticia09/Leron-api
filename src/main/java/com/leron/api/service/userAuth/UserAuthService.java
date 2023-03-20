package com.leron.api.service.userAuth;

import com.leron.api.mapper.UserAuthMapper.UserAuthMapper;
import com.leron.api.model.DTO.userAuth.UserAuthRequest;
import com.leron.api.model.DTO.userAuth.UserAuthRespose;
import com.leron.api.model.entities.UserAuthEntity;
import com.leron.api.repository.UserAuthRepository;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataRequest;
import com.leron.api.responses.DataResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class UserAuthService {
    private final UserAuthRepository userAuthRepository;

    public UserAuthService(UserAuthRepository userAuthRepository) {
        this.userAuthRepository = userAuthRepository;
    }

    public DataListResponse<UserAuthRespose> getUser(){
        return UserAuthMapper.userEntitiesToDataListResponse(userAuthRepository.findAll());
    }

    public DataResponse<UserAuthRespose> create(DataRequest<UserAuthRequest> userRequest,
                                             String locale,
                                             String authorization) throws ApplicationBusinessException {
        DataResponse<UserAuthRespose> response = new DataResponse<>();
        //UserValidator.validatorUser(userRequest);

        UserAuthEntity user = UserAuthMapper.createUserFromUserRequest(userRequest.getData());
        userAuthRepository.save(user);
        UserAuthRespose userResponse = UserAuthMapper.createUserResponse(user);
        response.setData(userResponse);
        response.setMessage("Sucesso");
        return response;
    }

    public List<String> validate(UserAuthRequest requestCreation){
        AtomicReference<Boolean> response = new AtomicReference<>(false);
                List<String> user = userAuthRepository.validate(requestCreation.getLogin(), requestCreation.getPassword());

        return user;
    }

}
