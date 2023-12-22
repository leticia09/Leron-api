package com.leron.api.service.expense;

import com.leron.api.mapper.expense.ExpenseMapper;
import com.leron.api.model.DTO.expense.ExpenseRequest;
import com.leron.api.model.DTO.expense.ExpenseResponse;
import com.leron.api.model.DTO.graphic.DataSet;
import com.leron.api.model.DTO.graphic.GraphicResponse;
import com.leron.api.model.entities.*;
import com.leron.api.repository.*;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataResponse;
import com.leron.api.validator.expense.ValidatorExpense;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class ExpenseService {
    final ExpenseRepository expenseRepository;
    final MemberRepository memberRepository;
    final RegisterBankRepository bankRepository;
    final BankMovementRepository bankMovementRepository;
    final AccountRepository accountRepository;
    final CardRepository cardRepository;
    final MoneyRepository moneyRepository;
    final CardFinancialEntityRepository cardFinancialEntityRepository;

    public ExpenseService(ExpenseRepository expenseRepository, MemberRepository memberRepository, RegisterBankRepository bankRepository, BankMovementRepository bankMovementRepository, AccountRepository accountRepository, CardRepository cardRepository, MoneyRepository moneyRepository, CardFinancialEntityRepository cardFinancialEntityRepository) {
        this.expenseRepository = expenseRepository;
        this.memberRepository = memberRepository;
        this.bankRepository = bankRepository;
        this.bankMovementRepository = bankMovementRepository;
        this.accountRepository = accountRepository;
        this.cardRepository = cardRepository;
        this.moneyRepository = moneyRepository;
        this.cardFinancialEntityRepository = cardFinancialEntityRepository;
    }

    public DataResponse<ExpenseResponse> create(List<ExpenseRequest> request) throws ApplicationBusinessException {
        DataResponse<ExpenseResponse> response = new DataResponse<>();
        List<Expense> expenses = expenseRepository.findAllByUserAuthIdAndDeletedFalse(request.get(0).getUserAuthId());
        List<Account> accounts = accountRepository.findAllByUserAuthIdAndDeletedFalse(request.get(0).getUserAuthId());
        List<Card> cards = cardRepository.findByUserAuthId(request.get(0).getUserAuthId());
        ValidatorExpense.validatorCreation(request, expenses);
        request.forEach(res -> {
            if (!res.getHasSplitExpense() && !res.getHasFixed()) {
                saveValuesToExpenses(res, cards, accounts);
            } else {
                expenseRepository.save(ExpenseMapper.requestToEntity(res));
            }
        });

        response.setSeverity("success");
        response.setMessage("success");
        return response;
    }

    private void saveValuesToExpenses(ExpenseRequest res, List<Card> cards, List<Account> accounts) {
        if (res.getPaymentForm().equalsIgnoreCase("Débito")) {
            Optional<Card> cardOptional = cards.stream().filter(card -> card.getFinalNumber().equals(res.getFinalCard())).findFirst();
            if (cardOptional.isPresent()) {
                Optional<Account> account = accounts.stream().filter(ac -> ac.getId().equals(cardOptional.get().getAccount().getId())).findFirst();
                if (account.isPresent()) {
                    Expense expenseSave = expenseRepository.save(ExpenseMapper.requestToEntity(res, account.get()));
                    BigDecimal valueUpdated = account.get().getValue().subtract(new BigDecimal(res.getValue().replace(",", ".")));
                    account.get().setValue(valueUpdated);
                    accountRepository.save(account.get());
                    bankMovementRepository.save(ExpenseMapper.createBankMovement(res, account.get(), expenseSave));
                }
            }

        }

        if (res.getPaymentForm().equalsIgnoreCase("Pix")) {
            Expense expenseSave = expenseRepository.save(ExpenseMapper.requestToEntity(res));
            Optional<Account> account = accountRepository.findById(res.getAccountId());
            if (account.isPresent()) {
                BigDecimal valueUpdated = account.get().getValue().subtract(new BigDecimal(res.getValue().replace(",", ".")));
                account.get().setValue(valueUpdated);
                accountRepository.save(account.get());
                bankMovementRepository.save(ExpenseMapper.createBankMovement(res, account.get(), expenseSave));
            }

        }
        if (res.getPaymentForm().equalsIgnoreCase("Dinheiro")) {
            Expense expenseSave = expenseRepository.save(ExpenseMapper.requestToEntity(res));
            Optional<Money> money = moneyRepository.findById(res.getMoneyId());
            if (money.isPresent()) {
                BigDecimal valueUpdated = money.get().getValue().subtract(new BigDecimal(res.getValue().replace(",", ".")));
                money.get().setValue(valueUpdated);
                moneyRepository.save(money.get());
                bankMovementRepository.save(ExpenseMapper.createBankMovementMoney(res, money.get(), expenseSave));
            }

        }

        if (res.getPaymentForm().equalsIgnoreCase("Vale")) {
            Expense expenseSave = expenseRepository.save(ExpenseMapper.requestToEntity(res));
            Optional<CardFinancialEntity> card = cardFinancialEntityRepository.findById(res.getCardId());
            if (card.isPresent()) {
                BigDecimal valueUpdated = card.get().getBalance().subtract(new BigDecimal(res.getValue().replace(",", ".")));
                card.get().setBalance(valueUpdated);
                cardFinancialEntityRepository.save(card.get());
                bankMovementRepository.save(ExpenseMapper.createBankMovementFinancialEntity(res, card.get(), expenseSave));
            }
        }
    }

    public DataListResponse<ExpenseResponse> list(Long userAuthId, int month, int year) {
        List<Expense> expense = expenseRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);
        List<Member> members = memberRepository.findAllByUserAuthIdAndDeletedFalseOrderByNameAsc(userAuthId);
        List<Card> cards = cardRepository.findByUserAuthId(userAuthId);
        List<BankMovement> bankMovements = bankMovementRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);
        return ExpenseMapper.entityToResponse(expense, members, cards, bankMovements, month, year);
    }

    public DataListResponse<ExpenseResponse> list(Long userAuthId) {
        List<Expense> expense = expenseRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);
        List<Member> members = memberRepository.findAllByUserAuthIdAndDeletedFalseOrderByNameAsc(userAuthId);
        List<Bank> banks = bankRepository.findByUserAuthId(userAuthId);
        List<BankMovement> bankMovements = bankMovementRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);
        return ExpenseMapper.entityToResponse(expense, members, banks, bankMovements);
    }

    public DataResponse<GraphicResponse> getData(Long authId, int month, int year) {
        DataResponse<GraphicResponse> response = new DataResponse<>();

        List<Member> members = memberRepository.findAllByUserAuthIdAndDeletedFalseAndStatusOrderByNameAsc(authId, "ACTIVE");
        List<Expense> expenses = expenseRepository.findAllByUserAuthIdAndDeletedFalse(authId);
        List<BankMovement> bankMovements = bankMovementRepository.findAllByUserAuthIdAndDeletedFalse(authId);

        BigDecimal receiveTotal = BigDecimal.ZERO;
        BigDecimal receiveOk = BigDecimal.ZERO;
        BigDecimal receiveHoldOn = BigDecimal.ZERO;
        BigDecimal receiveNotOk = BigDecimal.ZERO;

        GraphicResponse graphicResponse = new GraphicResponse();

        ArrayList<DataSet> dataSets = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        for (Member member : members) {
            DataSet dataSet = new DataSet();
            ArrayList<BigDecimal> data = new ArrayList<>(Collections.nCopies(labels.size(), BigDecimal.ZERO));

            for (Expense expense : expenses) {
                AtomicInteger movementMonth = new AtomicInteger();
                AtomicInteger movementYear = new AtomicInteger();
                final BigDecimal[] valueReceived = {BigDecimal.ZERO};

                String period = month + "/" + year;

                List<BankMovement> bankMovementList = bankMovements.stream()
                        .filter(bm -> Objects.equals(
                                bm.getExpenseId(), expense.getId()) &&
                                bm.getReferencePeriod().equalsIgnoreCase(period) &&
                                bm.getType().equalsIgnoreCase("Saída")
                        ).collect(Collectors.toList());


                BigDecimal value = bankMovementList.stream().map(BankMovement::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);

                if (member.getId().equals(expense.getOwnerId())) {
                    int labelIndex = labels.indexOf(expense.getMacroGroup());
                    if (labelIndex == -1) {
                        labels.add(expense.getMacroGroup());
                        data.add(value);
                    } else {
                        data.set(labelIndex, data.get(labelIndex).add(value));
                    }

//                    if(!GetStatusPayment.getStatus(entrance, bankMovementList, month, year).equalsIgnoreCase("Não Iniciada")) {
//                        receiveTotal = receiveTotal.add(entrance.getSalary());
//                    }
//                    receiveOk = receiveOk.add(value);


//                    if (bankMovementList.isEmpty()) {
//                        String status = GetStatusPayment.getStatus(entrance, bankMovementList, month, year);
//
//                        if (status.equalsIgnoreCase("Aguardando")) {
//                            receiveHoldOn = receiveHoldOn.add(entrance.getSalary());
//                        }
//
//                        if (status.equalsIgnoreCase("pendente")) {
//                            receiveNotOk = receiveNotOk.add(entrance.getSalary());
//                        }
//                    }
                }

            }

            if (!data.isEmpty()) {
                dataSet.setLabel(member.getName());
                dataSet.setBackgroundColor(member.getColor());
                dataSet.setBorderColor(member.getColor());
                dataSet.setData(data);
                dataSets.add(dataSet);
            }

        }

        graphicResponse.setDataSet(dataSets);
        graphicResponse.setLabels(labels);
        graphicResponse.setTotal1(new BigDecimal(100));
        graphicResponse.setTotal2(new BigDecimal(200));
        graphicResponse.setTotal3(new BigDecimal(300));
        graphicResponse.setTotal4(new BigDecimal(400));

        response.setData(graphicResponse);

        return response;
    }
}
