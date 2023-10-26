package com.leron.api.mapper;

import com.leron.api.model.DTO.points.PointsRequest;
import com.leron.api.model.DTO.points.PointsResponse;
import com.leron.api.model.entities.Score;
import com.leron.api.responses.DataListResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
            if(Objects.nonNull(res.getPointsExpirationDate())) {
                entity.setPointsExpirationDate(res.getPointsExpirationDate());
            } else {
                entity.setPointsExpirationDate(null);
            }


            scoreList.add(entity);
        });

        return scoreList;
    }

    public static PointsResponse createPointsResponse (Score entity) {

        PointsResponse response = new PointsResponse();
        response.setProgram(entity.getProgram());
        response.setId(entity.getId());
        response.setStatus(entity.getStatus());

        return response;
    }
}
