package com.leron.api.service.expense;

import com.leron.api.mapper.expense.ExpenseMapper;
import com.leron.api.model.DTO.expense.*;
import com.leron.api.model.DTO.graphic.DataSet;
import com.leron.api.model.DTO.graphic.GraphicResponse;
import com.leron.api.model.DTO.graphic.LabelTooltip;
import com.leron.api.model.DTO.graphic.Tooltip;
import com.leron.api.model.DTO.macroGroup.MacroGroupResponse;
import com.leron.api.model.entities.*;
import com.leron.api.repository.*;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataResponse;
import com.leron.api.service.macroGroup.MacroGroupService;
import com.leron.api.utils.GetStatusPayment;
import com.leron.api.validator.expense.ValidatorExpense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.leron.api.utils.FormatDate.populateMonths;
import static com.leron.api.utils.GetStatusPayment.getExpensesWithStatus;

@Service
public class ExpenseService {
    @Autowired
    MacroGroupService macroGroupService;

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
        List<Money> moneyList = moneyRepository.findAllByUserAuthIdAndDeletedFalse(request.get(0).getUserAuthId());
        List<CardFinancialEntity> cardFinancialEntityList = cardFinancialEntityRepository.findAllByUserAuthIdAndDeletedFalse(request.get(0).getUserAuthId());
        ValidatorExpense.validatorCreation(request, expenses);
        request.forEach(res -> {
            if (!res.getHasSplitExpense() && !res.getHasFixed()) {
                saveValuesToExpenses(res, cards, accounts, moneyList, cardFinancialEntityList);
            } else {
                expenseRepository.save(ExpenseMapper.requestToEntity(res, cards, moneyList, cardFinancialEntityList));

            }
        });

        response.setSeverity("success");
        response.setMessage("success");
        return response;
    }

    private void saveValuesToExpenses(ExpenseRequest res, List<Card> cards, List<Account> accounts, List<Money> moneyList, List<CardFinancialEntity> cardFinancialEntityList) {
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
            Expense expenseSave = expenseRepository.save(ExpenseMapper.requestToEntity(res, cards, moneyList, cardFinancialEntityList));
            Optional<Account> account = accountRepository.findById(res.getAccountId());
            if (account.isPresent()) {
                BigDecimal valueUpdated = account.get().getValue().subtract(new BigDecimal(res.getValue().replace(",", ".")));
                account.get().setValue(valueUpdated);
                accountRepository.save(account.get());
                bankMovementRepository.save(ExpenseMapper.createBankMovement(res, account.get(), expenseSave));
            }

        }
        if (res.getPaymentForm().equalsIgnoreCase("Dinheiro")) {
            Expense expenseSave = expenseRepository.save(ExpenseMapper.requestToEntity(res, cards, moneyList, cardFinancialEntityList));
            Optional<Money> money = moneyRepository.findById(res.getMoneyId());
            if (money.isPresent()) {
                BigDecimal valueUpdated = money.get().getValue().subtract(new BigDecimal(res.getValue().replace(",", ".")));
                money.get().setValue(valueUpdated);
                moneyRepository.save(money.get());
                bankMovementRepository.save(ExpenseMapper.createBankMovementMoney(res, money.get(), expenseSave));
            }

        }

        if (res.getPaymentForm().equalsIgnoreCase("Vale")) {
            Expense expenseSave = expenseRepository.save(ExpenseMapper.requestToEntity(res, cards, moneyList, cardFinancialEntityList));
            Optional<CardFinancialEntity> card = cardFinancialEntityRepository.findById(res.getCardId());
            if (card.isPresent()) {
                BigDecimal valueUpdated = card.get().getBalance().subtract(new BigDecimal(res.getValue().replace(",", ".")));
                card.get().setBalance(valueUpdated);
                cardFinancialEntityRepository.save(card.get());
                bankMovementRepository.save(ExpenseMapper.createBankMovementFinancialEntity(res, card.get(), expenseSave));
            }
        }

        if (res.getPaymentForm().equalsIgnoreCase("Crédito")) {
            expenseRepository.save(ExpenseMapper.requestToEntityList(res, cards));
        }
    }

    public DataResponse<ExpenseManagementResponse> getManagementData(Long userAuthId, int month, int year, List<Long> owners) {
        DataResponse<ExpenseManagementResponse> response = new DataResponse<>();
        ExpenseManagementResponse expenseManagementResponse = new ExpenseManagementResponse();

        List<ExpenseResponse> expenseResponses = list(userAuthId, month, year, owners);

        expenseManagementResponse.setExpenseResponseList(expenseResponses);
        expenseManagementResponse.setGraphicResponseData(getData(userAuthId, month, year, owners));
        expenseManagementResponse.setGraphicResponseDetails(getDataDetails(userAuthId, month, year, owners, expenseResponses));

        response.setData(expenseManagementResponse);
        return response;
    }

    public List<ExpenseResponse> list(Long userAuthId, int month, int year, List<Long> owners) {
        List<Expense> expense = expenseRepository.findAllByUserAuthIdAndDeletedFalseOrderByDateBuyDesc(userAuthId);
        List<Member> members = memberRepository.findMemberByIdsAndUserAuthId(userAuthId, owners);
        List<Card> cards = cardRepository.findByUserAuthId(userAuthId);
        List<BankMovement> bankMovements = bankMovementRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);
        List<Account> accounts = accountRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);
        List<Money> moneyList = moneyRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);
        List<CardFinancialEntity> cardFinancialEntityList = cardFinancialEntityRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);
        return ExpenseMapper.entityToResponse(expense, members, cards, bankMovements, month, year, accounts, moneyList, cardFinancialEntityList);
    }

    public GraphicResponse getDataDetails(Long authId, int month, int year, List<Long> owners, List<ExpenseResponse> expenses) {

        List<Member> members = memberRepository.findMemberByIdsAndUserAuthId(authId, owners);
        List<BankMovement> bankMovements = bankMovementRepository.findAllByUserAuthIdAndDeletedFalse(authId);
        List<MacroGroupResponse> macroGroupResponses = macroGroupService.listData(authId);

        BigDecimal receiveTotal = BigDecimal.ZERO;
        BigDecimal receiveOk = BigDecimal.ZERO;
        BigDecimal receiveHoldOn = BigDecimal.ZERO;
        BigDecimal receiveNotOk = BigDecimal.ZERO;

        GraphicResponse graphicResponse = new GraphicResponse();

        ArrayList<DataSet> dataSets = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        List<LabelTooltip> tooltips = new ArrayList<>();

        for (Member member : members) {
            DataSet dataSet = new DataSet();
            ArrayList<BigDecimal> data = new ArrayList<>(Collections.nCopies(labels.size(), BigDecimal.ZERO));
            ArrayList<Tooltip> tooltipList = new ArrayList<>();

            LabelTooltip labelTooltipObject = new LabelTooltip();
            labelTooltipObject.setLabel(member.getName());

            for (MacroGroupResponse macroGroup : macroGroupResponses) {
                BigDecimal totalPerGroup = BigDecimal.ZERO;
                ArrayList<String> tooltipLabel = new ArrayList<>();
                Tooltip tooltip = new Tooltip();
                List<ExpenseResponse> expenseResponseList = expenses.stream().filter(ex -> ex.getMacroGroup().equalsIgnoreCase(macroGroup.getName())).collect(Collectors.toList());
                tooltipLabel.add("");
                List<ObjectTemp> objectListTemp = new ArrayList<>();
                for (ExpenseResponse expense : expenseResponseList) {
                    if (member.getId().equals(expense.getOwnerId())) {
                        tooltip.setName(expense.getMacroGroup());
                        ObjectTemp objectTemp = new ObjectTemp();

                        objectTemp.setSpecificGroup(expense.getSpecificGroup());
                        objectTemp.setCurrency(expense.getCurrency());

                        if (!expense.getHasSplitExpense() && !expense.getStatus().equalsIgnoreCase("confirmado")) {
                            objectTemp.setValue(expense.getValue());
                            totalPerGroup = totalPerGroup.add(expense.getValue());
                        } else if (!expense.getStatus().equalsIgnoreCase("confirmado")) {
                            objectTemp.setValue(expense.getPartValue());
                            totalPerGroup = totalPerGroup.add(expense.getPartValue());
                        } else {
                            objectTemp.setValue(expense.getValuePaid());
                            totalPerGroup = totalPerGroup.add(expense.getValuePaid());
                        }
                        objectListTemp.add(objectTemp);
                    }
                }

                for (SpecificGroup specificGroup : macroGroup.getSpecificGroups()) {
                    List<ObjectTemp> objectList = objectListTemp.stream()
                            .filter(ob -> ob.getSpecificGroup().equalsIgnoreCase(specificGroup.getName()))
                            .collect(Collectors.toList());

                    BigDecimal totalPerSpecific = objectList.stream()
                            .map(ObjectTemp::getValue)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    if (!objectList.isEmpty()) {
                        tooltipLabel.add(objectList.get(0).getSpecificGroup() + ": " + objectList.get(0).getCurrency() + " " + totalPerSpecific);
                    }
                }


                tooltip.setTooltipLabel(tooltipLabel);
                tooltipList.add(tooltip);
                tooltipLabel.add("TOTAL: " + totalPerGroup);
            }

            labelTooltipObject.setTooltipList(tooltipList);
            tooltips.add(labelTooltipObject);


            for (ExpenseResponse expense : expenses) {
                String monthValidate = "" + month;
                if (month < 10) {
                    monthValidate = "0" + month;
                }
                String period = monthValidate + "/" + year;

                List<BankMovement> bankMovementList = bankMovements.stream()
                        .filter(bm -> Objects.equals(bm.getExpenseId(), expense.getId()) &&
                                bm.getUserAuthId().equals(authId) &&
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

                    if (!expense.getStatus().equalsIgnoreCase("Não Iniciada") && !expense.getStatus().isEmpty()) {
                        if (expense.getStatus().equalsIgnoreCase("Confirmado")) {
                            receiveTotal = receiveTotal.add(value);
                        } else if (expense.getHasSplitExpense()) {
                            BigDecimal c = expense.getValue().divide(new BigDecimal(expense.getQuantityPart()), MathContext.DECIMAL32);
                            receiveTotal = receiveTotal.add(c);
                        } else {
                            receiveTotal = receiveTotal.add(expense.getValue());
                        }

                    }

                    if (expense.getStatus().equalsIgnoreCase("Confirmado")) {
                        receiveOk = receiveOk.add(value);
                    }

                    if (expense.getStatus().equalsIgnoreCase("Aguardando")) {
                        if (expense.getHasSplitExpense()) {
                            BigDecimal c = expense.getValue().divide(new BigDecimal(expense.getQuantityPart()), MathContext.DECIMAL32);
                            receiveHoldOn = receiveHoldOn.add(c);
                        } else {
                            receiveHoldOn = receiveHoldOn.add(expense.getValue());
                        }
                    }

                    if (expense.getStatus().equalsIgnoreCase("pendente")) {
                        if (expense.getHasSplitExpense()) {
                            BigDecimal c = expense.getValue().divide(new BigDecimal(expense.getQuantityPart()), MathContext.DECIMAL32);
                            receiveNotOk = receiveNotOk.add(c);
                        } else {
                            receiveNotOk = receiveNotOk.add(expense.getValue());
                        }
                    }
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
        graphicResponse.setTotal1(receiveTotal);
        graphicResponse.setTotal2(receiveOk);
        graphicResponse.setTotal3(receiveHoldOn);
        graphicResponse.setTotal4(receiveNotOk);
        graphicResponse.setTooltipLabel(tooltips);

        return graphicResponse;
    }

    public GraphicResponse getData(Long authId, int month, int year, List<Long> owners) {
        List<Member> members = memberRepository.findMemberByIdsAndUserAuthId(authId, owners);
        List<Expense> expenses = expenseRepository.findAllByUserAuthIdAndDeletedFalseOrderByDateBuyDesc(authId);
        List<BankMovement> bankMovements = bankMovementRepository.findAllByUserAuthIdAndDeletedFalse(authId);

        BigDecimal receiveTotal = BigDecimal.ZERO;
        BigDecimal receiveOk = BigDecimal.ZERO;
        BigDecimal receiveHoldOn = BigDecimal.ZERO;
        BigDecimal receiveNotOk = BigDecimal.ZERO;

        GraphicResponse graphicResponse = new GraphicResponse();

        ArrayList<DataSet> dataSets = new ArrayList<>();
        ArrayList<String> labels = populateMonths();


        for (Member member : members) {
            BigDecimal[] months = new BigDecimal[12];

            for (int i = 0; i < 12; i++) {
                months[i] = new BigDecimal(BigInteger.ZERO);
            }
            DataSet dataSet = new DataSet();
            ArrayList<BigDecimal> data = new ArrayList<>();

            for (int i = 0; i < 12; i++) {
                for (Expense expense : expenses) {
                    int monthValue = i + 1;

                    if (member.getId().equals(expense.getOwnerId())) {
                        String monthValidate = (monthValue < 10) ? "0" + monthValue : "" + monthValue;
                        String period = monthValidate + "/" + year;

                        List<BankMovement> bankMovementList = bankMovements.stream()
                                .filter(bm -> Objects.equals(bm.getExpenseId(), expense.getId()) &&
                                        bm.getUserAuthId().equals(authId) &&
                                        bm.getReferencePeriod().equalsIgnoreCase(period) &&
                                        bm.getType().equalsIgnoreCase("Saída")
                                ).collect(Collectors.toList());

                        BigDecimal value = bankMovementList.stream().map(BankMovement::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);

                        String status = GetStatusPayment.getStatus(expense, bankMovementList, i + 1, year);

                        if (!status.equalsIgnoreCase("Não Iniciada") && !status.isEmpty()) {
                            expense.setStatus(status);
                            if (status.equalsIgnoreCase("Confirmado")) {
                                months[i] = months[i].add(value);
                            } else {
                                BigDecimal valueToAdd = expense.getHasSplitExpense() ?
                                        expense.getValue().divide(new BigDecimal(expense.getQuantityPart()), MathContext.DECIMAL32) :
                                        expense.getValue();

                                months[i] = months[i].add(valueToAdd);
                            }

                        }

                        if (!status.equalsIgnoreCase("Não Iniciada") && !status.isEmpty() && month == monthValue) {
                            if (status.equalsIgnoreCase("Confirmado")) {
                                if (expense.getPaymentForm().equalsIgnoreCase("vale")) {
                                    receiveTotal = receiveTotal.add(expense.getValue());
                                } else {
                                    receiveTotal = receiveTotal.add(value);
                                }

                            } else if (expense.getHasSplitExpense()) {
                                BigDecimal c = expense.getValue().divide(new BigDecimal(expense.getQuantityPart()), MathContext.DECIMAL32);
                                receiveTotal = receiveTotal.add(c);
                            } else {
                                receiveTotal = receiveTotal.add(expense.getValue());
                            }

                        }

                        if (status.equalsIgnoreCase("Confirmado") && month == monthValue) {
                            if (expense.getPaymentForm().equalsIgnoreCase("vale")) {
                                receiveOk = receiveOk.add(expense.getValue());
                            } else {
                                receiveOk = receiveOk.add(value);
                            }
                        }

                        if (status.equalsIgnoreCase("Aguardando") && month == monthValue) {
                            if (expense.getHasSplitExpense()) {
                                BigDecimal c = expense.getValue().divide(new BigDecimal(expense.getQuantityPart()), MathContext.DECIMAL32);
                                receiveHoldOn = receiveHoldOn.add(c);
                            } else {
                                receiveHoldOn = receiveHoldOn.add(expense.getValue());
                            }
                        }

                        if (status.equalsIgnoreCase("pendente") && month == monthValue) {
                            if (expense.getHasSplitExpense()) {
                                BigDecimal c = expense.getValue().divide(new BigDecimal(expense.getQuantityPart()), MathContext.DECIMAL32);
                                receiveNotOk = receiveNotOk.add(c);
                            } else {
                                receiveNotOk = receiveNotOk.add(expense.getValue());
                            }
                        }
                    }

                }
            }

            data.addAll(Arrays.asList(months));

            dataSet.setLabel(member.getName());
            dataSet.setBackgroundColor(member.getColor().replace("1)", "0.6)"));
            dataSet.setBorderColor(member.getColor());
            dataSet.setFill(true);
            dataSet.setData(data);
            dataSets.add(dataSet);
        }

        graphicResponse.setDataSet(dataSets);
        graphicResponse.setLabels(labels);
        graphicResponse.setTotal1(receiveTotal);
        graphicResponse.setTotal2(receiveOk);
        graphicResponse.setTotal3(receiveHoldOn);
        graphicResponse.setTotal4(receiveNotOk);

        return graphicResponse;
    }

    public DataListResponse<ExpensePeriodResponse> listPeriod(Long userAuthId, String period, Long owner, List<Long> cards) {
        DataListResponse<ExpensePeriodResponse> response = new DataListResponse<>();
        List<BankMovement> bankMovements = bankMovementRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);
        List<ExpensePeriodResponse> responseList = new ArrayList<>();

        cards.forEach(card -> {
            String[] part = period.split("/");
            int month = Integer.parseInt(part[0]);
            int year = Integer.parseInt(part[1]);

            String monthToSend = (month < 10) ? "0" + month : String.valueOf(month);

            Card card1 = cardRepository.findCardByFinalNumber(userAuthId, card);
            List<Expense> expenses = expenseRepository.findAllByUserAuthIdAndBankIdAndAccountIdAndFinalCard(userAuthId, card1.getAccount().getBank().getId(), card1.getAccount().getId(), card);

            for (Expense ex : expenses) {
                ExpensePeriodResponse expensePeriodResponse = new ExpensePeriodResponse();
                List<BankMovement> bankMovementList = bankMovements.stream()
                        .filter(bm -> Objects.equals(bm.getExpenseId(), ex.getId()) &&
                                bm.getUserAuthId().equals(userAuthId) &&
                                bm.getReferencePeriod().equalsIgnoreCase(monthToSend + "/" + year) &&
                                bm.getType().equalsIgnoreCase("Saída")
                        ).collect(Collectors.toList());

                String status = GetStatusPayment.getStatus(ex, bankMovementList, month, year);
                if (!status.equalsIgnoreCase("Não Iniciada") && !status.equalsIgnoreCase("Confirmado") && !status.equalsIgnoreCase("")) {
                    if ("Crédito".equalsIgnoreCase(ex.getPaymentForm())) {
                        BigDecimal partValue = ex.getValue().divide(BigDecimal.valueOf(ex.getQuantityPart()), 2, RoundingMode.HALF_UP);
                        expensePeriodResponse.setId(ex.getId());
                        expensePeriodResponse.setLocal(ex.getLocal());
                        expensePeriodResponse.setFinalCard(ex.getFinalCard());
                        expensePeriodResponse.setOwnerId(ex.getOwnerId());
                        expensePeriodResponse.setValue(partValue.toString());
                        expensePeriodResponse.setCurrency(card1.getAccount().getCurrency());
                        responseList.add(expensePeriodResponse);
                    }
                }
            }
        });
        response.setData(responseList);
        return response;
    }

    public DataListResponse<ExpenseResponse> list(Long userAuthId) {
        List<Expense> expense = expenseRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);
        List<Member> members = memberRepository.findAllByUserAuthIdAndDeletedFalseOrderByNameAsc(userAuthId);
        List<Bank> banks = bankRepository.findByUserAuthId(userAuthId);
        List<BankMovement> bankMovements = bankMovementRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);
        return ExpenseMapper.entityToResponse(expense, members, banks, bankMovements);
    }

    public DataResponse<BigDecimal> getAmountByRegisterBank(Long userAuthId, Long bankId, Long accountId, List<String> cardListRequest, String period) {
        DataResponse<BigDecimal> response = new DataResponse<>();
        AtomicReference<BigDecimal> amount = new AtomicReference<>(new BigDecimal(BigInteger.ZERO));
        List<BankMovement> bankMovements = bankMovementRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);

        cardListRequest.forEach(card -> {
            String[] part = period.split("/");
            int month = Integer.parseInt(part[0]);
            int year = Integer.parseInt(part[1]);

            List<Expense> expenses = expenseRepository.findAllByUserAuthIdAndBankIdAndAccountIdAndFinalCard(userAuthId, bankId, accountId, Long.parseLong(card));
            for (Expense ex : expenses) {
                List<BankMovement> bankMovementList = bankMovements.stream()
                        .filter(bm -> Objects.equals(bm.getExpenseId(), ex.getId()) &&
                                bm.getUserAuthId().equals(userAuthId) &&
                                bm.getReferencePeriod().equalsIgnoreCase(period) &&
                                bm.getType().equalsIgnoreCase("Saída")
                        ).collect(Collectors.toList());

                String status = GetStatusPayment.getStatus(ex, bankMovementList, month, year);
                if (!status.equalsIgnoreCase("Não Iniciada") && !status.equalsIgnoreCase("Confirmado") && !status.equalsIgnoreCase("")) {
                    if ("Crédito".equalsIgnoreCase(ex.getPaymentForm())) {
                        BigDecimal partValue = ex.getValue().divide(BigDecimal.valueOf(ex.getQuantityPart()), 2, RoundingMode.HALF_UP);
                        amount.updateAndGet(currentAmount -> currentAmount.add(partValue));
                    }
                }
            }
        });

        response.setData(amount.get());
        response.setSeverity("success");
        response.setMessage("success");
        return response;
    }

    public Expense getById(Long id) {
        return expenseRepository.getById(id);
    }

    public List<Expense> getExpenseFixed(Long userAuthId, int month, int year, Long owner) {
        List<Expense> expenses = expenseRepository.findAllByUserAuthIdAndDeletedFalseAndHasFixedTrueAndOwnerId(userAuthId, owner);
        List<BankMovement> bankMovements = bankMovementRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);

        expenses.forEach(ex -> {
            String monthValidate = "" + month;
            if (month < 10) {
                monthValidate = "0" + month;
            }
            String period = monthValidate + "/" + year;
            List<BankMovement> bankMovementList = bankMovements
                    .stream()
                    .filter(bm -> Objects.nonNull(bm.getExpenseId()) &&
                            bm.getReferencePeriod().equalsIgnoreCase(period) &&
                            bm.getExpenseId().equals(ex.getId()
                            )).collect(Collectors.toList());
            String status = GetStatusPayment.getStatus(ex, bankMovementList, month, year);
            ex.setStatus(status);

        });
        return expenses.stream().filter(ex -> (
                Objects.isNull(ex.getStatus()) ||
                        ex.getStatus().equalsIgnoreCase("Aguardando") ||
                        ex.getStatus().equalsIgnoreCase("Pendente")
        )).collect(Collectors.toList());
    }

    public List<Expense> getExpenseHasSplit(Long userAuthId, int month, int year, Long owner) {
        List<Expense> expenses = expenseRepository.findAllByUserAuthIdAndDeletedFalseAndHasSplitExpenseTrueAndOwnerIdAndStatusIsNull(userAuthId, owner);
        List<Expense> expenseFiltered = expenses.stream().filter(ex -> !ex.getPaymentForm().equalsIgnoreCase("crédito")).collect(Collectors.toList());
        List<BankMovement> bankMovements = bankMovementRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);

        expenseFiltered.forEach(ex -> {
            String monthValidate = "" + month;
            if (month < 10) {
                monthValidate = "0" + month;
            }
            String period = monthValidate + "/" + year;
            List<BankMovement> bankMovementList = bankMovements
                    .stream()
                    .filter(bm -> Objects.nonNull(bm.getExpenseId()) &&
                            bm.getReferencePeriod().equalsIgnoreCase(period) &&
                            bm.getExpenseId().equals(ex.getId())
                    ).collect(Collectors.toList());
            String status = GetStatusPayment.getStatus(ex, bankMovementList, month, year);
            ex.setStatus(status);
        });


        return expenseFiltered.stream().filter(ex -> (Objects.isNull(ex.getStatus()) || ex.getStatus().equalsIgnoreCase("Aguardando") ||
                ex.getStatus().equalsIgnoreCase("Pendente")
        )).collect(Collectors.toList());
    }
}
