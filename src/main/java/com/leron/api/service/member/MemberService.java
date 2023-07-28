package com.leron.api.service.member;

import com.leron.api.mapper.member.MemeberMapper;
import com.leron.api.model.DTO.user.UserRequest;
import com.leron.api.model.DTO.user.UserResponse;
import com.leron.api.model.entities.MemberEntity;
import com.leron.api.repository.MemberRepository;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataRequest;
import com.leron.api.responses.DataResponse;
import com.leron.api.validator.user.MemberValidator;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MemberService {

    private final MemberRepository userRepository;

    public MemberService(MemberRepository userRepository) {
        this.userRepository = userRepository;
    }

    public DataListResponse<UserResponse> list(Long userAuthId){
        return MemeberMapper.userEntitiesToDataListResponse(userRepository.findAllByAuthUserId(userAuthId));
    }

    public DataResponse<List<UserResponse>> create(DataRequest<List<UserRequest>> userRequest,
                                             String locale,
                                             String authorization) throws ApplicationBusinessException {
        DataResponse<List<UserResponse>> response = new DataResponse<>();

        userRequest.getData().forEach(user -> {
            try {
                MemberValidator.validateUser(user);
            } catch (ApplicationBusinessException e) {
                e.printStackTrace();
            }
        });

        List<MemberEntity> entities = userRepository.saveAll(MemeberMapper.createUserFromUserRequest(userRequest.getData()));
        List<UserResponse> userResponse = MemeberMapper.createUserResponse(entities);
        response.setData(userResponse);
        response.setMessage("Sucesso");
        return response;
    }

}
