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
import java.util.Optional;


@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository userRepository) {
        this.memberRepository = userRepository;
    }

    public DataListResponse<MemberResponse> list(Long userAuthId){
        return MemeberMapper.userEntitiesToDataListResponse(memberRepository.findAllByUserAuthIdAndDeletedFalseOrderByNameAsc(userAuthId));
    }

    public DataResponse<List<MemberResponse>> create(DataRequest<List<MemberRequest>> userRequest,
                                                     String locale,
                                                     String authorization) throws ApplicationBusinessException {
        DataResponse<List<MemberResponse>> response = new DataResponse<>();

        List<MemberEntity> members = memberRepository.findAllByUserAuthIdAndDeletedFalseOrderByNameAsc(userRequest.getData().get(0).getUserAuthId());

        MemberValidator.validateUser(userRequest.getData(),members);


        List<MemberEntity> entities = memberRepository.saveAll(MemeberMapper.createUserFromUserRequest(userRequest.getData()));
        List<MemberResponse> userResponse = MemeberMapper.createUserResponse(entities);
        response.setData(userResponse);
        response.setMessage("Sucesso");
        return response;
    }

    public DataResponse<MemberResponse> edit(MemberResponse request) throws ApplicationBusinessException {
        DataResponse<MemberResponse> response = new DataResponse<>();

        //MemberValidator.validateUser(userRequest.getData(),members);

        Optional<MemberEntity> currentMember = memberRepository.findById(request.getId());
        MemberEntity entities = memberRepository.save(MemeberMapper.createUserFromMemberEditRequest(request, currentMember.get()));
        MemberResponse userResponse = MemeberMapper.createMemberResponse(entities);
        response.setData(userResponse);
        response.setMessage("success");
        return response;
    }

    public DataResponse<MemberResponse> delete(Long id, Long userAuthId) throws ApplicationBusinessException {
        DataResponse<MemberResponse> response = new DataResponse<>();
        MemberEntity currentMember = memberRepository.findMemberByIdAndUserAuthId(id, userAuthId);
        currentMember.setDeleted(true);
        MemberEntity entities = memberRepository.save(currentMember);
        MemberResponse userResponse = MemeberMapper.createMemberResponse(entities);
        response.setData(userResponse);
        response.setMessage("success");
        return response;
    }
}
