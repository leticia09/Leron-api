package com.leron.api.service.financialEntity;

import com.leron.api.mapper.financialEntity.FinancialEntityMapper;
import com.leron.api.model.DTO.financialEntity.FinancialEntityRequest;
import com.leron.api.model.DTO.financialEntity.FinancialEntityResponse;
import com.leron.api.model.entities.*;
import com.leron.api.repository.*;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataResponse;
import com.leron.api.validator.financialEntity.FinancialEntityValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FinancialEntityService {
    final FinancialEntityRepository financialEntityRepository;

    final CardFinancialEntityRepository cardFinancialEntityRepository;

    private final BankMovementRepository bankMovementRepository;

    private final EntranceRepository entranceRepository;

    private final ExpenseRepository expenseRepository;

    public FinancialEntityService(FinancialEntityRepository financialEntityRepository, CardFinancialEntityRepository cardFinancialEntityRepository, BankMovementRepository bankMovementRepository, EntranceRepository entranceRepository, ExpenseRepository expenseRepository) {
        this.financialEntityRepository = financialEntityRepository;
        this.cardFinancialEntityRepository = cardFinancialEntityRepository;
        this.bankMovementRepository = bankMovementRepository;
        this.entranceRepository = entranceRepository;
        this.expenseRepository = expenseRepository;
    }

    public DataResponse<FinancialEntityResponse> create(FinancialEntityRequest request) throws ApplicationBusinessException {
        DataResponse<FinancialEntityResponse> response = new DataResponse<>();

        List<FinancialEntity> financial = financialEntityRepository.findAllByUserAuthIdAndDeletedFalse(request.getUserAuthId());

        FinancialEntityValidator.validate(request, financial);

        FinancialEntity financialEntityToSave = FinancialEntityMapper.requestToEntity(request);

        FinancialEntity financialSave = financialEntityRepository.save(financialEntityToSave);

        for(CardFinancialEntity card : financialSave.getCardFinancialEntityList()) {
            FinancialEntity financialEntity = new FinancialEntity();
            financialEntity.setName(financialSave.getName());
            financialEntity.setId(financialSave.getId());
            card.setFinancialEntity(financialEntity);
        }
        cardFinancialEntityRepository.saveAll(financialSave.getCardFinancialEntityList());

        response.setSeverity("success");
        response.setMessage("success");
        return response;
    }

    public DataListResponse<FinancialEntityResponse> list(Long userId) {
        List<FinancialEntity> moneyList = financialEntityRepository.findAllByUserAuthIdAndDeletedFalse(userId);
        return FinancialEntityMapper.entityToResponse(moneyList);
    }

    public DataResponse<FinancialEntityResponse> delete(Long moneyId) {
        DataResponse<FinancialEntityResponse> response = new DataResponse<>();

        Optional<FinancialEntity> financialEntity = financialEntityRepository.findById(moneyId);

        financialEntity.ifPresent(value -> {
            List<BankMovement> bankMovementList = bankMovementRepository.findAllByUserAuthIdAndFinancialEntityId(financialEntity.get().getUserAuthId(), financialEntity.get().getId());
            if(!bankMovementList.isEmpty()) {
                bankMovementList.forEach(bankMovement -> {
                    bankMovement.setDeleted(true);
                });
                bankMovementRepository.saveAll(bankMovementList);
            }

            List<Entrance> entrances = entranceRepository.findAllByUserAuthIdAndFinancialEntityId(financialEntity.get().getUserAuthId(), financialEntity.get().getId());
            if(!entrances.isEmpty()) {
                entrances.forEach(entrance -> {
                    entrance.setDeleted(true);
                });
                entranceRepository.saveAll(entrances);
            }

            List<Expense> expenses = expenseRepository.findAllByUserAuthIdAndFinancialEntityId(financialEntity.get().getUserAuthId(), financialEntity.get().getId());
            if(!expenses.isEmpty()) {
                expenses.forEach(expense -> {
                    expense.setDeleted(true);
                });
                expenseRepository.saveAll(expenses);
            }

            value.setDeleted(true);
            financialEntityRepository.save(value);
        });

        response.setSeverity("success");
        response.setMessage("success");
        return response;
    }

    public DataResponse<FinancialEntityResponse> edit(FinancialEntityResponse request) throws ApplicationBusinessException {
        DataResponse<FinancialEntityResponse> response = new DataResponse<>();

        Optional<FinancialEntity> money = financialEntityRepository.findById(request.getId());
        List<FinancialEntity> currentMoneys = financialEntityRepository.findAllByUserAuthIdAndDeletedFalse(money.get().getUserAuthId());

        FinancialEntityValidator.validateEdit(currentMoneys, request);

//        money.ifPresent(value -> {
//            value.setValue(request.getValue());
//            value.setCurrency(request.getCurrency());
//            value.setOwnerId(request.getOwnerId());
//            financialEntityRepository.save(value);
//        });

        response.setSeverity("success");
        response.setMessage("success");
        return response;
    }
}
