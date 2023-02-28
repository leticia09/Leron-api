package com.leron.api.service.expense;

import com.leron.api.mapper.expense.ExpenseMapper;
import com.leron.api.model.DTO.expense.ExpenseRequest;
import com.leron.api.model.DTO.expense.ExpenseResponse;
import com.leron.api.model.entities.BankEntity;
import com.leron.api.model.entities.CardEntity;
import com.leron.api.model.entities.ExpenseEntity;
import com.leron.api.model.entities.UserEntity;
import com.leron.api.repository.BankRepository;
import com.leron.api.repository.CardRepository;
import com.leron.api.repository.ExpenseRepository;
import com.leron.api.repository.UserRepository;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataRequest;
import com.leron.api.responses.DataResponse;
import com.leron.api.validator.expense.ExpenseValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final BankRepository bankRepository;

    public ExpenseService(ExpenseRepository expenseRepository, CardRepository cardRepository, UserRepository userRepository, BankRepository bankRepository) {
        this.expenseRepository = expenseRepository;
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
        this.bankRepository = bankRepository;
    }

    public DataListResponse<ExpenseResponse> list(){
        List<ExpenseEntity> expenseEntities = expenseRepository.findAll();
        List<CardEntity> cardEntities = cardRepository.findAll();
        List<UserEntity> userEntityList =  userRepository.findAll();

        DataListResponse<ExpenseResponse> response  = ExpenseMapper.expenseEntitiesToDataListResponse(expenseEntities, cardEntities, userEntityList);

        return response;
    }

    public DataResponse<ExpenseResponse> create(DataRequest<ExpenseRequest> expenseRequest) throws ApplicationBusinessException {
        DataResponse<ExpenseResponse> response = new DataResponse<>();
        ExpenseValidator.validatorExpense(expenseRequest);

        ExpenseEntity expense = ExpenseMapper.createExpenseFromExpenseRequest(expenseRequest.getData());
        expenseRepository.save(expense);
        ExpenseResponse expenseResponse = ExpenseMapper.createExpenseResponse(expense);
        response.setData(expenseResponse);
        response.setMessage("Sucesso");
        return response;
    }
}
