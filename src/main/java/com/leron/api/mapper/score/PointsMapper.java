package com.leron.api.mapper.score;

import com.leron.api.model.DTO.points.PointsRequest;
import com.leron.api.model.DTO.points.PointsResponse;
import com.leron.api.model.DTO.points.TransferRequest;
import com.leron.api.model.DTO.points.TypeScoreDTO;
import com.leron.api.model.entities.Member;
import com.leron.api.model.entities.Score;
import com.leron.api.model.entities.Transfer;
import com.leron.api.responses.DataListResponse;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PointsMapper {
    public static DataListResponse<PointsResponse> pointsEntitiesToDataListResponse(List<Score> entityList, List<Member> memberEntities){
        DataListResponse<PointsResponse> response = new DataListResponse<>();
        List<PointsResponse> responseList = new ArrayList<>();

        for (Score entity : entityList) {
            PointsResponse responses = new PointsResponse();

            responses.setId(entity.getId());
            responses.setProgram(entity.getProgram());
            if(entity.getStatus().equals("ACTIVE")) {
                responses.setStatus(1L);
            }
            if(entity.getStatus().equals("INACTIVE")) {
                responses.setStatus(2L);
            }
            responses.setUserAuthId(entity.getUserAuthId());
            responses.setValue(entity.getValue());
            responses.setPointsExpirationDate(entity.getPointsExpirationDate());
            responses.setTypeOfScore(entity.getTypeOfScore());

            Optional<Member> ownerMember = memberEntities.stream()
                    .filter(member -> member.getId().equals(entity.getOwnerId()))
                    .findFirst();

            if(ownerMember.isPresent()) {
                responses.setOwner(ownerMember.get());
                responseList.add(responses);
            }
        }
        response.setData(responseList);
        return response;
    }

    public static List<Score> createPointsFromRevenueRequest(List<PointsRequest> request) {
        List<Score> scoreList = new ArrayList<>();
        request.forEach(res -> {
            Score entity = new Score();
            entity.setUserAuthId(res.getUserAuthId());
            entity.setProgram(res.getProgram().substring(0, 1).toUpperCase() + res.getProgram().substring(1).toLowerCase());
            entity.setStatus("ACTIVE");
            entity.setValue(res.getValue());
            if(res.getTypeOfScore().equals("1")) {
                entity.setTypeOfScore("Milhas");
            }
            if(res.getTypeOfScore().equals("2")) {
                entity.setTypeOfScore("Pontos");
            }

            if(Objects.nonNull(res.getPointsExpirationDate())) {
                entity.setPointsExpirationDate(res.getPointsExpirationDate());
            } else {
                entity.setPointsExpirationDate(null);
            }
            entity.setCreatedIn(new Date());
            entity.setDeleted(false);
            entity.setOwnerId(res.getOwnerId());

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

    public static List<TypeScoreDTO> mapScoreToObjectList(List<Score> data) {
        List<TypeScoreDTO> resultList = new ArrayList<>();

        for (Score row : data) {
            TypeScoreDTO typeScoreDTO = new TypeScoreDTO();
            typeScoreDTO.setId(row.getId());
            typeScoreDTO.setDescription(row.getProgram());


            resultList.add(typeScoreDTO);
        }

        return resultList;
    }


}
