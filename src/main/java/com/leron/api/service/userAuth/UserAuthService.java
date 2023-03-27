package com.leron.api.service.userAuth;

import com.leron.api.mapper.userAuth.UserAuthMapper;
import com.leron.api.model.DTO.userAuth.UserAuthRequest;
import com.leron.api.model.DTO.userAuth.UserAuthRespose;
import com.leron.api.model.DTO.userAuth.UserValidResponse;
import com.leron.api.model.entities.UserAuthEntity;
import com.leron.api.repository.UserAuthRepository;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataRequest;
import com.leron.api.responses.DataResponse;
import com.leron.api.validator.user.UserAuthValidator;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public DataResponse<UserAuthRespose> create(DataRequest<UserAuthRequest> userRequest) throws ApplicationBusinessException {
        DataResponse<UserAuthRespose> response = new DataResponse<>();
        List<UserAuthEntity> currentUser = userAuthRepository.findAll();
        UserAuthValidator.validatorUser(userRequest, currentUser);
        UserAuthEntity user = UserAuthMapper.createUserFromUserRequest(userRequest.getData());
        userAuthRepository.save(user);
        UserAuthRespose userResponse = UserAuthMapper.createUserResponse(user);
        response.setData(userResponse);
        response.setMessage("Sucesso");
        return response;
    }

    public UserValidResponse validate(UserAuthRequest requestCreation){
        return userAuthRepository.validate(requestCreation.getLogin(), requestCreation.getPassword());
    }

}
