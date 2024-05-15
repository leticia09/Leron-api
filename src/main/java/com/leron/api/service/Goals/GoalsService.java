package com.leron.api.service.Goals;

import com.leron.api.mapper.Goals.GoalsMapper;
import com.leron.api.model.DTO.goals.GoalsManagementResponse;
import com.leron.api.model.DTO.goals.GoalsRequest;
import com.leron.api.model.DTO.goals.GoalsResponse;
import com.leron.api.model.DTO.graphic.GraphicResponse;
import com.leron.api.model.entities.*;
import com.leron.api.repository.*;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataResponse;
import com.leron.api.validator.Goal.ValidatorGoal;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoalsService {
    final GoalsRepository goalsRepository;
    final MemberRepository memberRepository;
    final RegisterBankRepository bankRepository;
    final BankMovementRepository bankMovementRepository;


    public GoalsService(GoalsRepository goalsRepository, MemberRepository memberRepository, RegisterBankRepository bankRepository, BankMovementRepository bankMovementRepository) {
        this.goalsRepository = goalsRepository;
        this.memberRepository = memberRepository;
        this.bankRepository = bankRepository;
        this.bankMovementRepository = bankMovementRepository;
    }

    public DataResponse<GoalsResponse> create(List<GoalsRequest> requestDTO, String locale, String authorization) throws ApplicationBusinessException {
        DataResponse<GoalsResponse> response = new DataResponse<>();

        List<Goals> goals = goalsRepository.findAllByUserAuthIdAndDeletedFalse(requestDTO.get(0).getUserAuthId());

        ValidatorGoal.validateCreation(requestDTO, goals);

        goalsRepository.saveAll(GoalsMapper.requestToEntity(requestDTO));

        response.setSeverity("success");
        response.setMessage("success");

        return response;
    }

    public DataResponse<GoalsManagementResponse> getManagementData(Long authId, int month, int year, List<Long> owners) {
        DataResponse<GoalsManagementResponse> response = new DataResponse<>();
        GoalsManagementResponse goalsManagementResponse = new GoalsManagementResponse();

        goalsManagementResponse.setGoalsResponseList(list(authId, month, year, owners));
        goalsManagementResponse.setGraphicResponseData(getData(authId, month, year, owners));
        goalsManagementResponse.setGraphicResponseDetails(getDataDetails(authId, month, year, owners));

        response.setData(goalsManagementResponse);
        return response;
    }


    public List<GoalsResponse> list(Long userAuthId, int month, int year, List<Long> owners) {
        List<Goals> goals = goalsRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);
        List<Member> members = memberRepository.findMemberByIdsAndUserAuthId(userAuthId, owners);
        List<Bank> banks = bankRepository.findByUserAuthId(userAuthId);
        List<BankMovement> bankMovements = bankMovementRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);
        return GoalsMapper.entityToResponse(goals, members, banks, bankMovements, month, year);
    }

    public GraphicResponse getData(Long authId, int month, int year, List<Long> owners) {
        GraphicResponse graphicResponse = new GraphicResponse();

        return graphicResponse;
    }

    public GraphicResponse getDataDetails(Long authId, int month, int year, List<Long> owners) {
        GraphicResponse graphicResponse = new GraphicResponse();

        return graphicResponse;
    }
}