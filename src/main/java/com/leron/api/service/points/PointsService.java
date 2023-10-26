package com.leron.api.service.points;

import com.leron.api.mapper.PointsMapper;
import com.leron.api.model.DTO.points.PointsRequest;
import com.leron.api.model.DTO.points.PointsResponse;
import com.leron.api.model.entities.*;
import com.leron.api.repository.PointsRepository;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataRequest;
import com.leron.api.responses.DataResponse;
import com.leron.api.validator.points.PointsValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointsService {

    private final PointsRepository pointsRepository;

    public PointsService(PointsRepository pointsRepository) {
        this.pointsRepository = pointsRepository;
    }

    public DataListResponse<PointsResponse> list(Long userAuthId) {
        List<Score> pointsEntityList = pointsRepository.findByUserAuthId(userAuthId);
        return PointsMapper.pointsEntitiesToDataListResponse(pointsEntityList);
    }

    public DataResponse<PointsResponse> create(DataRequest<List<PointsRequest>> request) throws ApplicationBusinessException {
        DataResponse<PointsResponse> response = new DataResponse<>();
        List<Score> pointsEntityList = pointsRepository.findByUserAuthId(request.getData().get(0).getUserAuthId());
        PointsValidator.validator(request.getData(), pointsEntityList);

        List<Score> entity = PointsMapper.createPointsFromRevenueRequest(request.getData());
        pointsRepository.saveAll(entity);
        response.setMessage("Sucesso");
        return response;
    }
}
