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

//    public DataListResponse<PointsResponse> list(Long userAuthId) {
//        List<PointsEntity> pointsEntityList = pointsRepository.findAllByAuthUserId(userAuthId);
//
//    }

    public DataResponse<PointsResponse> create(DataRequest<PointsRequest> request) throws ApplicationBusinessException {
        DataResponse<PointsResponse> response = new DataResponse<>();
        PointsValidator.validator(request);

        PointsEntity entity = PointsMapper.createPointsFromRevenueRequest(request.getData());
        pointsRepository.save(entity);
        response.setData(PointsMapper.createPointsResponse(entity));
        response.setMessage("Sucesso");
        return response;
    }
}
