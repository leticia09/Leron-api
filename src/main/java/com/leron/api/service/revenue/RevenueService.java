package com.leron.api.service.revenue;

import com.leron.api.mapper.revenue.RevenueMapper;
import com.leron.api.model.DTO.revenue.RevenueRequest;
import com.leron.api.model.DTO.revenue.RevenueResponse;
import com.leron.api.model.entities.*;
import com.leron.api.repository.BankAccountRepository;
import com.leron.api.repository.BankRepository;
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
    private final BankRepository bankRepository;
    private final BankAccountRepository bankAccountRepository;
    private final RevenueRepository revenueRepository;
    private final SalaryRepository salaryRepository;

    public RevenueService(BankRepository bankRepository, BankAccountRepository bankAccountRepository, RevenueRepository revenueRepository, SalaryRepository salaryRepository) {
        this.bankRepository = bankRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.revenueRepository = revenueRepository;
        this.salaryRepository = salaryRepository;
    }

    public DataListResponse<RevenueResponse> list(Long userAuthId){
        List<BankEntity> bankEntityList = bankRepository.findAllByAuthUserId(userAuthId);
        List<BankAccountEntity> bankAccountEntityList = bankAccountRepository.findAllByAuthUserId(userAuthId);
        List<RevenueEntity> revenueEntityList = revenueRepository.findAllByAuthUserId(userAuthId);
        List<SalaryEntity> salaryEntityList = salaryRepository.findAllByAuthUserId(userAuthId);

        DataListResponse<RevenueResponse> response  = RevenueMapper.revenueEntitiesToDataListResponse(revenueEntityList, bankEntityList, bankAccountEntityList, salaryEntityList);

        return response;
    }

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
