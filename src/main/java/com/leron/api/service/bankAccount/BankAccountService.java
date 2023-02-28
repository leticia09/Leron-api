package com.leron.api.service.bankAccount;

import com.leron.api.mapper.bankAccount.BankAccountMapper;
import com.leron.api.mapper.card.CardMapper;
import com.leron.api.model.DTO.bankAccount.BankAccountRequest;
import com.leron.api.model.DTO.bankAccount.BankAccountResponse;
import com.leron.api.model.DTO.card.CardDTO;
import com.leron.api.model.DTO.card.CardRequest;
import com.leron.api.model.DTO.card.CardResponse;
import com.leron.api.model.entities.BankAccountEntity;
import com.leron.api.model.entities.BankEntity;
import com.leron.api.model.entities.CardEntity;
import com.leron.api.model.entities.UserEntity;
import com.leron.api.repository.BankAccountRepository;
import com.leron.api.repository.BankRepository;
import com.leron.api.repository.UserRepository;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataRequest;
import com.leron.api.responses.DataResponse;
import com.leron.api.validator.bankAccount.BankAccountValidator;
import com.leron.api.validator.card.CardValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;
    private final BankRepository bankRepository;


    public BankAccountService(BankAccountRepository bankAccountRepository, UserRepository userRepository, BankRepository bankRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.userRepository = userRepository;
        this.bankRepository = bankRepository;
    }

    public DataListResponse<BankAccountResponse> list(){
        List<BankAccountEntity> bankAccountEntities = bankAccountRepository.findAll();
        List<BankEntity> bankEntityList = bankRepository.findAll();
        List<UserEntity> userEntityList =  userRepository.findAll();

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
}
