package com.leron.api.service.userAuth;

import com.leron.api.mapper.userAuth.UserAuthMapper;
import com.leron.api.model.DTO.userAuth.UserAuthRequest;
import com.leron.api.model.DTO.userAuth.UserAuthResponse;
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

@Service
public class UserAuthService {
    private final UserAuthRepository userAuthRepository;

    public UserAuthService(UserAuthRepository userAuthRepository) {
        this.userAuthRepository = userAuthRepository;
    }

    public DataListResponse<UserAuthResponse> getUser(){
        return UserAuthMapper.userEntitiesToDataListResponse(userAuthRepository.findAll());
    }

    public DataResponse<UserAuthResponse> create(DataRequest<UserAuthRequest> userRequest) throws ApplicationBusinessException {
        DataResponse<UserAuthResponse> response = new DataResponse<>();
        List<UserAuthEntity> currentUser = userAuthRepository.findAll();
        UserAuthValidator.validatorUser(userRequest, currentUser);
        UserAuthEntity user = UserAuthMapper.createUserFromUserRequest(userRequest.getData());
        userAuthRepository.save(user);
        UserAuthResponse userResponse = UserAuthMapper.createUserResponse(user);
        response.setData(userResponse);
        response.setMessage("success");
        return response;
    }

    public UserValidResponse validate(UserAuthRequest requestCreation){
        return userAuthRepository.validate(requestCreation.getLogin(), requestCreation.getPassword());
    }

}
