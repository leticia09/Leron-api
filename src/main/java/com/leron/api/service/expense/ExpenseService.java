package com.leron.api.service.expense;

import com.leron.api.mapper.expense.ExpenseMapper;
import com.leron.api.model.DTO.expense.ExpenseRequest;
import com.leron.api.model.DTO.expense.ExpenseResponse;
import com.leron.api.model.entities.ExpenseEntity;
import com.leron.api.model.entities.MemberEntity;
import com.leron.api.repository.CardRepository;
import com.leron.api.repository.ExpenseRepository;
import com.leron.api.repository.MemberRepository;
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
    private final MemberRepository userRepository;

    public ExpenseService(ExpenseRepository expenseRepository, CardRepository cardRepository, MemberRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
    }

    public DataListResponse<ExpenseResponse> list(Long userAuthId){
        List<ExpenseEntity> expenseEntities = expenseRepository.findAllByAuthUserId(userAuthId);
        List<MemberEntity> userEntityList =  userRepository.findAllByAuthUserId(userAuthId);

        DataListResponse<ExpenseResponse> response  = ExpenseMapper.expenseEntitiesToDataListResponse(expenseEntities, null, userEntityList);

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
