package com.leron.api.service.Goals;

import com.leron.api.mapper.Goals.GoalsMapper;
import com.leron.api.mapper.entrance.EntranceMapper;
import com.leron.api.model.DTO.Goals.GoalsRequest;
import com.leron.api.model.DTO.Goals.GoalsResponse;
import com.leron.api.model.DTO.entrance.EntranceRequest;
import com.leron.api.model.DTO.entrance.EntranceResponse;
import com.leron.api.model.entities.Entrance;
import com.leron.api.model.entities.Goals;
import com.leron.api.repository.GoalsRepository;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataResponse;
import com.leron.api.validator.Goal.ValidatorGoal;
import com.leron.api.validator.entrance.ValidatorEntrance;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoalsService {
    final GoalsRepository goalsRepository;

    public GoalsService(GoalsRepository goalsRepository) {
        this.goalsRepository = goalsRepository;
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

}
