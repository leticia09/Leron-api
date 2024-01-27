package com.leron.api.service.movementBank;

import com.leron.api.mapper.bankMovement.BankMovementMapper;
import com.leron.api.model.DTO.BankMovement.BankMovementResponse;
import com.leron.api.model.DTO.BankMovement.PaymentRequest;
import com.leron.api.model.DTO.BankMovement.ReceiveRequest;
import com.leron.api.model.DTO.BankMovement.TransferBankRequest;
import com.leron.api.model.DTO.expense.ExpensePeriodResponse;
import com.leron.api.model.DTO.graphic.DataSet;
import com.leron.api.model.DTO.graphic.GraphicResponse;
import com.leron.api.model.DTO.graphic.LabelTooltip;
import com.leron.api.model.DTO.graphic.Tooltip;
import com.leron.api.model.entities.*;
import com.leron.api.repository.*;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataResponse;
import com.leron.api.utils.GetStatusPayment;
import com.leron.api.validator.BankMovementValidator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovementBankService {
    private final MemberRepository memberRepository;
    private final RegisterBankRepository bankRepository;
    private final EntranceRepository entranceRepository;
    private final BankMovementRepository bankMovementRepository;
    private final AccountRepository accountRepository;
    private final FinancialEntityRepository financialEntityRepository;
    private final CardFinancialEntityRepository cardFinancialRepository;
    private final MoneyRepository moneyRepository;
    private final ExpenseRepository expenseRepository;
    private final CardRepository cardRepository;

    public MovementBankService(MemberRepository memberRepository, RegisterBankRepository bankRepository, EntranceRepository entranceRepository, BankMovementRepository bankMovementRepository, AccountRepository accountRepository, FinancialEntityRepository financialEntityRepository, CardFinancialEntityRepository cardFinancialRepository, MoneyRepository moneyRepository, ExpenseRepository expenseRepository, CardRepository cardRepository) {
        this.memberRepository = memberRepository;
        this.bankRepository = bankRepository;
        this.entranceRepository = entranceRepository;
        this.bankMovementRepository = bankMovementRepository;
        this.accountRepository = accountRepository;
        this.financialEntityRepository = financialEntityRepository;
        this.cardFinancialRepository = cardFinancialRepository;
        this.moneyRepository = moneyRepository;
        this.expenseRepository = expenseRepository;
        this.cardRepository = cardRepository;
    }

    public DataListResponse<BankMovementResponse> list(Long userAuthId) {
        List<BankMovement> bankMovements = bankMovementRepository.findAllByUserAuthIdAndDeletedFalseOrderByDateMovementDesc(userAuthId);
        return BankMovementMapper.entitiesToResponse(bankMovements);
    }

    public DataResponse<GraphicResponse> getData(Long authId) {
        DataResponse<GraphicResponse> response = new DataResponse<>();

        List<Member> members = memberRepository.findAllByUserAuthIdAndDeletedFalseAndStatusOrderByNameAsc(authId, "ACTIVE");
        List<Bank> banks = bankRepository.findByUserAuthId(authId);
        List<FinancialEntity> financialEntities = financialEntityRepository.findAllByUserAuthIdAndDeletedFalse(authId);
        List<Money> moneyList = moneyRepository.findAllByUserAuthIdAndDeletedFalse(authId);

        BigDecimal totalMoney = BigDecimal.ZERO;
        BigDecimal totalAvailable = BigDecimal.ZERO;
        BigDecimal totalGoal = BigDecimal.ZERO;
        BigDecimal totalDollar = BigDecimal.ZERO;

        GraphicResponse graphicResponse = new GraphicResponse();

        ArrayList<DataSet> dataSets = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        List<LabelTooltip> tooltips = new ArrayList<>();

        for (Member member : members) {
            DataSet dataSet = new DataSet();
            ArrayList<BigDecimal> data = new ArrayList<>(Collections.nCopies(labels.size(), BigDecimal.ZERO));
            ArrayList<Tooltip> tooltipList1 = new ArrayList<>();
            ArrayList<List<String>> tooltipList = new ArrayList<>();

            LabelTooltip labelTooltipObject = new LabelTooltip();
            labelTooltipObject.setLabel(member.getName());

            for (Bank bank : banks) {
                ArrayList<String> tooltipLabel = new ArrayList<>();
                Tooltip tooltip = new Tooltip();
                tooltipLabel.add("");
                BigDecimal totalAccount = new BigDecimal(BigInteger.ZERO);
                String currency = "";
                for (Account account : bank.getAccounts()) {
                    if (member.getId().equals(account.getMemberId())) {
                        int labelIndex = labels.indexOf(bank.getName());
                        tooltip.setName(bank.getName());
                        tooltipLabel.add(account.getAccountNumber() + ": " + account.getCurrency() + " " + account.getValue());
                        totalAccount = totalAccount.add(account.getValue());
                        currency = account.getCurrency();
                        if (labelIndex == -1) {
                            labels.add(bank.getName());
                            data.add(account.getValue());
                        } else {
                            data.set(labelIndex, data.get(labelIndex).add(account.getValue()));
                        }

                        if (account.getCurrency().equalsIgnoreCase("R$")) {
                            totalMoney = totalMoney.add(account.getValue());
                        }

                        if (account.getCurrency().equalsIgnoreCase("US$")) {
                            totalDollar = totalDollar.add(account.getValue());
                        }

                    }
                }
                if (!Objects.equals(currency, "")) {
                    tooltipLabel.add("Total: " + currency + " " + totalAccount);
                    tooltip.setTooltipLabel(tooltipLabel);
                    tooltipList1.add(tooltip);
                }


            }

            if (!financialEntities.isEmpty()) {
                for (FinancialEntity financialEntity : financialEntities) {
                    ArrayList<String> tooltipLabel = new ArrayList<>();
                    Tooltip tooltip = new Tooltip();
                    tooltipLabel.add("");
                    BigDecimal totalAccount = new BigDecimal(BigInteger.ZERO);
                    String currency = "";
                    for (CardFinancialEntity card : financialEntity.getCardFinancialEntityList()) {
                        if (member.getId().equals(card.getOwnerId())) {
                            int labelIndex = labels.indexOf(financialEntity.getName());
                            tooltip.setName(financialEntity.getName());
                            tooltipLabel.add(card.getCardName() + ": " + card.getCurrency() + " " + card.getBalance());
                            totalAccount = totalAccount.add(card.getBalance());
                            currency = card.getCurrency();
                            if (labelIndex == -1) {
                                labels.add(financialEntity.getName());
                                data.add(card.getBalance());
                            } else {
                                data.set(labelIndex, data.get(labelIndex).add(card.getBalance()));
                            }

                            if (card.getCurrency().equalsIgnoreCase("R$")) {
                                totalMoney = totalMoney.add(card.getBalance());
                            }

                            if (card.getCurrency().equalsIgnoreCase("US$")) {
                                totalDollar = totalDollar.add(card.getBalance());
                            }

                        }
                    }

                    if (!Objects.equals(currency, "")) {
                        tooltipLabel.add("Total: " + currency + " " + totalAccount);
                        tooltip.setTooltipLabel(tooltipLabel);
                        tooltipList1.add(tooltip);
                    }

                }
            }

            if (!moneyList.isEmpty()) {
                for (Money money : moneyList) {
                    ArrayList<String> tooltipLabel = new ArrayList<>();
                    Tooltip tooltip = new Tooltip();
                    tooltipLabel.add("");
                    BigDecimal totalAccount = new BigDecimal(BigInteger.ZERO);
                    String currency = "";

                    if (member.getId().equals(money.getOwnerId())) {
                        int labelIndex = labels.indexOf(money.getCurrency());
                        tooltip.setName(money.getCurrency());
                        tooltipLabel.add(money.getCurrency() + " " + money.getValue());
                        totalAccount = totalAccount.add(money.getValue());
                        currency = money.getCurrency();
                        if (labelIndex == -1) {
                            labels.add(money.getCurrency());
                            data.add(money.getValue());
                        } else {
                            data.set(labelIndex, data.get(labelIndex).add(money.getValue()));
                        }

                        if (money.getCurrency().equalsIgnoreCase("R$")) {
                            totalMoney = totalMoney.add(money.getValue());
                        }

                        if (money.getCurrency().equalsIgnoreCase("US$")) {
                            totalDollar = totalDollar.add(money.getValue());
                        }
                    }


                    if (!Objects.equals(currency, "")) {
                        tooltipLabel.add("Total: " + currency + " " + totalAccount);
                        tooltip.setTooltipLabel(tooltipLabel);
                        tooltipList1.add(tooltip);
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
            labelTooltipObject.setTooltipList(tooltipList1);
            tooltips.add(labelTooltipObject);
        }

        graphicResponse.setDataSet(dataSets);
        graphicResponse.setLabels(labels);
        graphicResponse.setTotal1(totalMoney);
        graphicResponse.setTotal2(totalAvailable);
        graphicResponse.setTotal3(totalGoal);
        graphicResponse.setTotal4(totalDollar);
        graphicResponse.setTooltipLabel(tooltips);

        response.setData(graphicResponse);

        return response;
    }

    public DataResponse<BankMovementResponse> createReceive(List<ReceiveRequest> request, Long userAuthId) throws ApplicationBusinessException {
        DataResponse<BankMovementResponse> response = new DataResponse<>();
        List<Entrance> entrances = entranceRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);
        List<BankMovement> bankMovementList = bankMovementRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);
        List<Account> accounts = accountRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);
        List<CardFinancialEntity> cardFinancialEntity = cardFinancialRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);
        List<Money> money = moneyRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);

        BankMovementValidator.validate(request, entrances, bankMovementList);

        request.forEach(res -> {
            if ((Objects.isNull(res.getBankId()) || res.getBankId() == 0) && (Objects.isNull(res.getAccountId()) || res.getAccountId() == 0)) {
                Account accountList = BankMovementMapper.receiveToAccount(res, accounts, entrances);

                CardFinancialEntity cardFinancialEntityList = BankMovementMapper.receiveToFinancial(res, cardFinancialEntity, entrances);

                Money moneyList = BankMovementMapper.receiveToMoney(res, entrances, userAuthId, money);

                BankMovement bankMovements = BankMovementMapper.receiveToBankMovement(res, entrances, userAuthId, accounts, cardFinancialEntity, moneyList);

                bankMovementRepository.save(bankMovements);

                if (accountList != null) {

                }


                if (cardFinancialEntityList != null) {
                    cardFinancialRepository.save(cardFinancialEntityList);
                }

                if (moneyList != null) {
                    moneyRepository.save(moneyList);
                }

            } else {
                Optional<Account> account = accountRepository.findById(res.getAccountId());
                if (account.isPresent()) {
                    BankMovement bankMovements = BankMovementMapper.receiveBankMovement(res, userAuthId, account.get().getCurrency(), account.get());
                    bankMovementRepository.save(bankMovements);

                    account.get().setValue(getBigDecimal(res, account.get()));
                    accountRepository.save(account.get());
                }

            }
        });


        response.setSeverity("success");
        response.setMessage("success");
        return response;
    }

    public DataResponse<BankMovementResponse> createPayment(List<PaymentRequest> request, Long userAuthId) throws ApplicationBusinessException {
        DataResponse<BankMovementResponse> response = new DataResponse<>();
        List<Expense> expenses = expenseRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);
        List<Account> accounts = accountRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);
        List<CardFinancialEntity> cardFinancialEntity = cardFinancialRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);
        List<Money> money = moneyRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);
        List<BankMovement> bankMovementList = bankMovementRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);
        request.forEach(res -> {
            if (Objects.isNull(res.getBankId()) && Objects.isNull(res.getAccountId()) && Objects.isNull(res.getCardId())) {

                Optional<Expense> expense = expenses.stream().filter(ex -> ex.getId().equals(res.getExpenseId())).findFirst();

                if (expense.isPresent()) {
                    if (Objects.nonNull(expense.get().getAccountId())) {
                        Account accountList = BankMovementMapper.paymentToAccount(res, accounts, expenses);
                        if (accountList != null) {
                            accountRepository.save(accountList);
                        }
                    }
                    Money moneyList = null;
                    if (Objects.nonNull(expense.get().getMoneyId())) {
                        moneyList = BankMovementMapper.payToMoney(res, expenses, userAuthId, money);
                    }

                    BankMovement bankMovements = BankMovementMapper.payToBankMovement(res, expenses, userAuthId, accounts, cardFinancialEntity, moneyList);

                    bankMovementRepository.save(bankMovements);

                    if (moneyList != null) {
                        moneyRepository.save(moneyList);
                    }

                    List<BankMovement> banks = bankMovementList.stream().filter(bm -> Objects.nonNull(bm.getExpenseId()) && bm.getExpenseId().equals(res.getExpenseId())).collect(Collectors.toList());

                    if (Objects.nonNull(expense.get().getQuantityPart()) && expense.get().getQuantityPart() == banks.size()) {
                        expense.get().setStatus("Quitado");
                        expenseRepository.save(expense.get());
                    }

                }

            } else {
                Account account = accountRepository.getById(res.getAccountId());

                for (Long card : res.getCardId()) {
                    String[] part = res.getReferencePeriod().split("/");
                    int month = Integer.parseInt(part[0]);
                    int year = Integer.parseInt(part[1]);

                    List<Expense> expenseList = expenseRepository.findAllByUserAuthIdAndBankIdAndAccountIdAndFinalCard(userAuthId, res.getBankId(), res.getAccountId(), card);

                    for (Expense ex : expenseList) {

                        Optional<ExpensePeriodResponse> expensePeriodResponse = res.getExpenseList().stream().filter(exp -> ex.getId().equals(exp.getId())).findFirst();

                        List<BankMovement> bankMovementList1 = bankMovementList.stream()
                                .filter(bm -> Objects.equals(bm.getExpenseId(), ex.getId()) &&
                                        bm.getUserAuthId().equals(userAuthId) &&
                                        bm.getReferencePeriod().equalsIgnoreCase(res.getReferencePeriod()) &&
                                        bm.getType().equalsIgnoreCase("Saída")
                                ).collect(Collectors.toList());

                        String status = GetStatusPayment.getStatus(ex, bankMovementList1, month, year);
                        if (ex.getPaymentForm().equalsIgnoreCase("crédito") &&
                                !status.equalsIgnoreCase("Não Iniciada") &&
                                !status.equalsIgnoreCase("Confirmado") &&
                                !status.equalsIgnoreCase("")) {
                            Card card1 = cardRepository.findCardByFinalNumber(userAuthId, card);

                            if (expensePeriodResponse.isPresent()) {
                                BankMovement bankMovements = BankMovementMapper.paymentCreditToBankMovement(res, ex, userAuthId, account, card1, expensePeriodResponse.get());
                                bankMovementRepository.save(bankMovements);
                            }
                        }
                    }
                }

                BigDecimal value = new BigDecimal(res.getValue().replace(",", "."));
                BigDecimal oldValue = account.getValue();
                BigDecimal newValue = oldValue.subtract(value);
                account.setChangedIn(new Date());
                account.setValue(newValue);
                accountRepository.save(account);

            }
        });

        response.setSeverity("success");
        response.setMessage("success");
        return response;
    }

    private static BigDecimal getBigDecimal(ReceiveRequest res, Account account) {
        String values = "";
        if (!Objects.equals(res.getValue(), "") && Objects.nonNull(res.getValue())) {
            values = res.getValue().replace(",", ".");
        }
        if (!Objects.equals(res.getSalary(), "") && Objects.nonNull(res.getSalary())) {
            values = res.getSalary().replace(account.getCurrency(), "").replace(",", ".").trim();
        }
        BigDecimal oldValue = account.getValue();
        BigDecimal requestValue = new BigDecimal(values);
        return oldValue.add(requestValue);
    }

    public DataResponse<BankMovementResponse> createTransfer(List<TransferBankRequest> request, Long userAuthId) throws ApplicationBusinessException {
        DataResponse<BankMovementResponse> response = new DataResponse<>();

        List<BankMovement> bankMovementList = bankMovementRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);
        List<Account> accounts = accountRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);

        BankMovementValidator.validateTransfer(request, bankMovementList);

        request.forEach(res -> {

            if ((Objects.nonNull(res.getBankDestinyId()) && res.getBankDestinyId() != 0) || (Objects.nonNull(res.getOwnerDestinyId()) && res.getOwnerDestinyId() != 0)) {
                List<Account> accountList = BankMovementMapper.transferToAccount(res, accounts);
                List<BankMovement> bankMovements = BankMovementMapper.transferToBankMovement(res, userAuthId, accounts);

                if (!bankMovements.isEmpty()) {
                    bankMovementRepository.saveAll(bankMovements);
                }

                if (!accountList.isEmpty()) {
                    accountRepository.saveAll(accountList);
                }
            } else {
                List<Account> accountList = BankMovementMapper.transferToAccountNotDestiny(res, accounts);
                BankMovement bankMovements = BankMovementMapper.transferToBankMovementNotDestiny(res, userAuthId, accounts);

                bankMovementRepository.save(bankMovements);

                if (!accountList.isEmpty()) {
                    accountRepository.saveAll(accountList);
                }
            }

        });


        response.setSeverity("success");
        response.setMessage("success");
        return response;
    }
}
