package com.leron.api.service.member;

import com.leron.api.mapper.member.MemeberMapper;
import com.leron.api.model.DTO.user.MemberRequest;
import com.leron.api.model.DTO.user.MemberResponse;
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

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository userRepository) {
        this.memberRepository = userRepository;
    }

    public DataListResponse<MemberResponse> list(Long userAuthId){
        return MemeberMapper.userEntitiesToDataListResponse(memberRepository.findAllByAuthUserId(userAuthId));
    }

    public DataResponse<List<MemberResponse>> create(DataRequest<List<MemberRequest>> userRequest,
                                                     String locale,
                                                     String authorization) throws ApplicationBusinessException {
        DataResponse<List<MemberResponse>> response = new DataResponse<>();

        userRequest.getData().forEach(user -> {
            try {
                MemberValidator.validateUser(user);
            } catch (ApplicationBusinessException e) {
                e.printStackTrace();
            }
        });

        List<MemberEntity> entities = memberRepository.saveAll(MemeberMapper.createUserFromUserRequest(userRequest.getData()));
        List<MemberResponse> userResponse = MemeberMapper.createUserResponse(entities);
        response.setData(userResponse);
        response.setMessage("Sucesso");
        return response;
    }

}
