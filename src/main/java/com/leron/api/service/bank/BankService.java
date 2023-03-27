package com.leron.api.service.bank;

import com.leron.api.mapper.bank.BankMapper;
import com.leron.api.model.DTO.bank.BankRequest;
import com.leron.api.model.DTO.bank.BankResponse;
import com.leron.api.model.entities.BankEntity;
import com.leron.api.repository.BankRepository;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataRequest;
import com.leron.api.responses.DataResponse;
import com.leron.api.validator.bank.BankValidator;
import org.springframework.stereotype.Service;

@Service
public class BankService {

    private final BankRepository bankRepository;

    public BankService(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    public DataListResponse<BankResponse> list(Long userAuthId){
        return BankMapper.bankEntitiesToDataListResponse(bankRepository.findAllByAuthUserId(userAuthId));
    }

    public DataResponse<BankResponse> create(DataRequest<BankRequest> userRequest) throws ApplicationBusinessException {
        DataResponse<BankResponse> response = new DataResponse<>();
        BankValidator.validatorBank(userRequest);

        BankEntity bank = BankMapper.createBankFromBankRequest(userRequest.getData());
        bankRepository.save(bank);
        BankResponse bankResponse = BankMapper.createBankResponse(bank);
        response.setData(bankResponse);
        response.setMessage("Sucesso");
        return response;
    }

}
