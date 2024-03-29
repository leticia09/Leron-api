package com.leron.api.mapper;

import com.leron.api.model.DTO.points.PointsRequest;
import com.leron.api.model.DTO.points.PointsResponse;
import com.leron.api.model.DTO.points.TransferRequest;
import com.leron.api.model.DTO.points.TypeScoreDTO;
import com.leron.api.model.entities.Score;
import com.leron.api.model.entities.Transfer;
import com.leron.api.responses.DataListResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
public class PointsMapper {
    public static DataListResponse<PointsResponse> pointsEntitiesToDataListResponse(List<Score> entityList){
        DataListResponse<PointsResponse> response = new DataListResponse<>();
        List<PointsResponse> responseList = new ArrayList<>();

        for (Score entity : entityList) {
            PointsResponse responses = new PointsResponse();

            responses.setId(entity.getId());
            responses.setProgram(entity.getProgram());
            responses.setStatus(entity.getStatus());
            responses.setUserAuthId(entity.getUserAuthId());
            responses.setValue(entity.getValue());
            responses.setPointsExpirationDate(entity.getPointsExpirationDate());
            responses.setTypeOfScore(entity.getTypeOfScore());
            responseList.add(responses);

        }
        response.setData(responseList);
        return response;
    }

    public static List<Score> createPointsFromRevenueRequest(List<PointsRequest> request) {
        List<Score> scoreList = new ArrayList<>();
        request.forEach(res -> {
            Score entity = new Score();
            entity.setUserAuthId(res.getUserAuthId());
            entity.setProgram(res.getProgram());
            entity.setStatus("ACTIVE");
            entity.setValue(res.getValue());
            entity.setTypeOfScore(res.getTypeOfScore());
            if(Objects.nonNull(res.getPointsExpirationDate())) {
                entity.setPointsExpirationDate(res.getPointsExpirationDate());
            } else {
                entity.setPointsExpirationDate(null);
            }


            scoreList.add(entity);
        });

        return scoreList;
    }

    public static Transfer TransferRequestToEntity(TransferRequest request) {
        Transfer transfer = new Transfer();
        transfer.setOriginProgramId(request.getOriginProgramId());
        transfer.setDestinyProgramId(request.getDestinyProgramId());
        transfer.setQuantity(request.getQuantity());
        transfer.setOriginValue(request.getOriginValue());
        transfer.setDestinyValue(request.getDestinyValue());
        if(Objects.nonNull(request.getBonus())) {
            transfer.setOriginProgramId(request.getOriginProgramId());
        }

        if(Objects.nonNull(request.getPointsExpirationDate())) {
            transfer.setPointsExpirationDate(request.getPointsExpirationDate());
        }

        transfer.setUserAuthId(request.getUserAuthId());
        transfer.setCreatedIn(new Date());

        return transfer;
    }

    public static List<TypeScoreDTO> mapToObjectList(List<Object[]> data) {
        List<TypeScoreDTO> resultList = new ArrayList<>();

        for (Object[] row : data) {
            if (row.length >= 2) {
                Long id = (Long) row[0];
                String description = (String) row[1];
                TypeScoreDTO typeScore = new TypeScoreDTO();
                typeScore.setId(id);
                typeScore.setDescription(description);
                resultList.add(typeScore);
            }
        }

        return resultList;
    }


}
