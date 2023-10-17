package com.leron.api.service.revenue;

import com.leron.api.mapper.revenue.RevenueMapper;
import com.leron.api.model.DTO.revenue.RevenueRequest;
import com.leron.api.model.DTO.revenue.RevenueResponse;
import com.leron.api.model.entities.*;
import com.leron.api.repository.RevenueRepository;
import com.leron.api.repository.SalaryRepository;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataRequest;
import com.leron.api.responses.DataResponse;
import com.leron.api.validator.revenue.RevenueValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RevenueService {
    private final RevenueRepository revenueRepository;
    private final SalaryRepository salaryRepository;

    public RevenueService(RevenueRepository revenueRepository, SalaryRepository salaryRepository) {
        this.revenueRepository = revenueRepository;
        this.salaryRepository = salaryRepository;
    }

//    public DataListResponse<RevenueResponse> list(Long userAuthId){
//        List<RevenueEntity> revenueEntityList = revenueRepository.findAllByAuthUserId(userAuthId);
//        List<SalaryEntity> salaryEntityList = salaryRepository.findAllByAuthUserId(userAuthId);
//
//        return RevenueMapper.revenueEntitiesToDataListResponse(
//                revenueEntityList,
//                bankEntityList,
//                bankAccountEntityList,
//                salaryEntityList
//        );
//    }

    public DataResponse<RevenueResponse> create(DataRequest<RevenueRequest> revenueRequest) throws ApplicationBusinessException {
        DataResponse<RevenueResponse> response = new DataResponse<>();
        RevenueValidator.validator(revenueRequest);

        RevenueEntity entity = RevenueMapper.createRevenueFromRevenueRequest(revenueRequest.getData());
        revenueRepository.save(entity);
        RevenueResponse revenueResponse = RevenueMapper.createRevenueResponse(entity);
        response.setData(revenueResponse);
        response.setMessage("Sucesso");
        return response;
    }
}
