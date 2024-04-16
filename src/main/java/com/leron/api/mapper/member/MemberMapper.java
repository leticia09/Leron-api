package com.leron.api.mapper.member;

import com.leron.api.model.DTO.user.MemberRequest;
import com.leron.api.model.DTO.user.MemberResponse;
import com.leron.api.model.entities.Member;
import com.leron.api.responses.DataListResponse;
import com.leron.api.utils.FormatName;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class MemberMapper {
    public static DataListResponse<MemberResponse> userEntitiesToDataListResponse(List<Member> userEntities) {
        DataListResponse<MemberResponse> response = new DataListResponse<>();
        List<MemberResponse> responseList = new ArrayList<>();

        for (Member user : userEntities) {
            MemberResponse userDTO = new MemberResponse();
            userDTO.setColor(user.getColor());
            userDTO.setId(user.getId());
            userDTO.setName(user.getName());
            userDTO.setUserAuthId(user.getUserAuthId());
            if(user.getStatus().equals("ACTIVE")) {
                userDTO.setStatus(1L);
            }
            if(user.getStatus().equals("INACTIVE")) {
                userDTO.setStatus(2L);
            }

            responseList.add(userDTO);
        }
        response.setData(responseList);
        return response;
    }

    public static List<Member> createUserFromUserRequest(List<MemberRequest> userRequest) {
        List<Member> entities = new ArrayList<>();
        userRequest.forEach(user -> {
            Member entity = new Member();
            entity.setColor(user.getColor());
            entity.setName(FormatName.firstUpper(user.getName()));
            entity.setStatus("ACTIVE");
            entity.setCreatedIn(new Date());
            entity.setUserAuthId(user.getUserAuthId());
            entity.setDeleted(false);
            entities.add(entity);
        });


        return entities;
    }

    public static Member createUserFromMemberEditRequest(MemberResponse request, Member currentMember) {
        currentMember.setName(FormatName.firstUpper(request.getName()));
        currentMember.setChangedIn(new Date());
        currentMember.setColor(request.getColor());
        if(request.getStatus() == 1L) {
            currentMember.setStatus("ACTIVE");
        }
        if(request.getStatus() == 2L) {
            currentMember.setStatus("INACTIVE");
        }

        return currentMember;
    }


    public static List<MemberResponse> createUserResponse(List<Member> entities) {
        List<MemberResponse> responses = new ArrayList<>();
        entities.forEach(entity -> {
            MemberResponse userResponse = new MemberResponse();
            userResponse.setName(entity.getName());
            userResponse.setId(userResponse.getId());
            if(entity.getStatus().equals("ACTIVE")) {
                userResponse.setStatus(1L);
            }
            if(entity.getStatus().equals("INACTIVE")) {
                userResponse.setStatus(2L);
            }
            userResponse.setUserAuthId(userResponse.getUserAuthId());
            userResponse.setColor(entity.getColor());
            responses.add(userResponse);
        });


        return responses;
    }

    public static MemberResponse createMemberResponse(Member entity) {
        MemberResponse response = new MemberResponse();
        response.setName(entity.getName());
        response.setId(entity.getId());
        if(entity.getStatus().equals("ACTIVE")) {
            response.setStatus(1L);
        }
        if(entity.getStatus().equals("INACTIVE")) {
            response.setStatus(2L);
        }
        response.setUserAuthId(entity.getUserAuthId());
        return response;
    }

}
