package com.leron.api.service.bankAccount;

import com.leron.api.mapper.bankAccount.BankAccountMapper;
import com.leron.api.model.DTO.bankAccount.BankAccountRequest;
import com.leron.api.model.DTO.bankAccount.BankAccountResponse;
import com.leron.api.model.entities.BankAccountEntity;
import com.leron.api.model.entities.BankEntity;
import com.leron.api.model.entities.MemberEntity;
import com.leron.api.repository.BankAccountRepository;
import com.leron.api.repository.BankRepository;
import com.leron.api.repository.MemberRepository;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataRequest;
import com.leron.api.responses.DataResponse;
import com.leron.api.validator.bankAccount.BankAccountValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final MemberRepository userRepository;
    private final BankRepository bankRepository;


    public BankAccountService(BankAccountRepository bankAccountRepository, MemberRepository userRepository, BankRepository bankRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.userRepository = userRepository;
        this.bankRepository = bankRepository;
    }

    public DataListResponse<BankAccountResponse> list(Long userAuthId){
        List<BankAccountEntity> bankAccountEntities = bankAccountRepository.findAllByAuthUserId(userAuthId);
        List<BankEntity> bankEntityList = bankRepository.findAllByAuthUserId(userAuthId);
        List<MemberEntity> userEntityList =  userRepository.findAllByAuthUserId(userAuthId);

        DataListResponse<BankAccountResponse> response  = BankAccountMapper.bankAccountEntitiesToDataListResponse(bankAccountEntities, bankEntityList, userEntityList);

        return response;
    }

    public DataResponse<BankAccountResponse> create(DataRequest<BankAccountRequest> bankAccountRequestDataRequest) throws ApplicationBusinessException {
        DataResponse<BankAccountResponse> response = new DataResponse<>();

        BankAccountValidator.validatorBankAccount(bankAccountRequestDataRequest);

        BankAccountEntity bankAccount = BankAccountMapper.createBankAccountFromBankAccountRequest(bankAccountRequestDataRequest.getData());
        bankAccountRepository.save(bankAccount);
        BankAccountResponse bankAccountResponse = BankAccountMapper.createBankAccountResponse(bankAccount);
        response.setData(bankAccountResponse);
        response.setMessage("Sucesso");
        return response;
    }

    public DataListResponse<BankAccountResponse> listById(Long userAuthId, Long bankId){
        List<BankEntity> bankEntityList = bankRepository.findAllByAuthUserId(userAuthId);
        List<MemberEntity> userEntityList =  userRepository.findAllByAuthUserId(userAuthId);
        List<BankAccountEntity> bankAccountEntities = bankAccountRepository.findAllByAuthUserIdAndBankId(userAuthId, bankId);

        return BankAccountMapper.bankAccountEntitiesToDataListResponse(bankAccountEntities, bankEntityList, userEntityList);
    }
}
