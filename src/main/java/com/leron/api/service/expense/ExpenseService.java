package com.leron.api.service.expense;

import com.leron.api.mapper.expense.ExpenseMapper;
import com.leron.api.model.DTO.expense.ExpenseRequest;
import com.leron.api.model.DTO.expense.ExpenseResponse;
import com.leron.api.model.entities.*;
import com.leron.api.repository.*;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataResponse;
import com.leron.api.validator.expense.ValidatorExpense;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {
    final ExpenseRepository expenseRepository;
    final MemberRepository memberRepository;
    final RegisterBankRepository bankRepository;
    final BankMovementRepository bankMovementRepository;
    final AccountRepository accountRepository;

    public ExpenseService(ExpenseRepository expenseRepository, MemberRepository memberRepository, RegisterBankRepository bankRepository, BankMovementRepository bankMovementRepository, AccountRepository accountRepository) {
        this.expenseRepository = expenseRepository;
        this.memberRepository = memberRepository;
        this.bankRepository = bankRepository;
        this.bankMovementRepository = bankMovementRepository;
        this.accountRepository = accountRepository;
    }

    public DataResponse<ExpenseResponse> create(List<ExpenseRequest> request) throws ApplicationBusinessException {
        DataResponse<ExpenseResponse> response = new DataResponse<>();
        List<Expense> expenses = expenseRepository.findAllByUserAuthIdAndDeletedFalse(request.get(0).getUserAuthId());
        List<Account> accounts = accountRepository.findAllByUserAuthIdAndDeletedFalse(request.get(0).getUserAuthId());
        ValidatorExpense.validatorCreation(request, expenses);
// TODO: QUANDO FOR DÉBITO REGISTRAR UMA MOVIMENTAÇÃO BANCÁRIA E RETIRAR O DINHEIRO DA CONTA
        request.forEach(res -> {
            if(res.getPaymentForm().equalsIgnoreCase("Débito")) {
                for (Account account : accounts) {
                    Optional<Card> accountFiltered = account
                            .getCards().stream().filter(ca -> ca.getId().equals(res.getCardId())).findFirst();
                    if(accountFiltered.isPresent() && accountFiltered.get().getAccount().equals(account.getId())) {
                        BigDecimal valueUpdated =  account.getValue().subtract(new BigDecimal(res.getValue().replace(",",".")));
                        account.setValue(valueUpdated);
                        accountRepository.save(account);
                    }
                }
            }
        });

        expenseRepository.saveAll(ExpenseMapper.requestToEntity(request));

        response.setSeverity("success");
        response.setMessage("success");
        return response;
    }

    public DataListResponse<ExpenseResponse> list(Long userAuthId, int month, int year) {
        List<Expense> entrances = expenseRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);
        List<Member> members = memberRepository.findAllByUserAuthIdAndDeletedFalseOrderByNameAsc(userAuthId);
        List<Bank> banks = bankRepository.findByUserAuthId(userAuthId);
        List<BankMovement> bankMovements = bankMovementRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);
        return ExpenseMapper.entityToResponse(entrances, members, banks, bankMovements, month, year);
    }

    public DataListResponse<ExpenseResponse> list(Long userAuthId) {
        List<Expense> entrances = expenseRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);
        List<Member> members = memberRepository.findAllByUserAuthIdAndDeletedFalseOrderByNameAsc(userAuthId);
        List<Bank> banks = bankRepository.findByUserAuthId(userAuthId);
        List<BankMovement> bankMovements = bankMovementRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);
        return ExpenseMapper.entityToResponse(entrances, members, banks, bankMovements);
    }
}
