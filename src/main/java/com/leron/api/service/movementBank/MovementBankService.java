package com.leron.api.service.movementBank;

import com.leron.api.mapper.bankMovement.BankMovementMapper;
import com.leron.api.model.DTO.BankMovement.BankMovementResponse;
import com.leron.api.model.DTO.BankMovement.ReceiveRequest;
import com.leron.api.model.DTO.graphic.DataSet;
import com.leron.api.model.DTO.graphic.GraphicResponse;
import com.leron.api.model.DTO.graphic.LabelTooltip;
import com.leron.api.model.DTO.graphic.Tooltip;
import com.leron.api.model.entities.*;
import com.leron.api.repository.*;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataResponse;
import com.leron.api.validator.BankMovementValidator;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

    public MovementBankService(MemberRepository memberRepository, RegisterBankRepository bankRepository, EntranceRepository entranceRepository, BankMovementRepository bankMovementRepository, AccountRepository accountRepository, FinancialEntityRepository financialEntityRepository, CardFinancialEntityRepository cardFinancialRepository, MoneyRepository moneyRepository) {
        this.memberRepository = memberRepository;
        this.bankRepository = bankRepository;
        this.entranceRepository = entranceRepository;
        this.bankMovementRepository = bankMovementRepository;
        this.accountRepository = accountRepository;
        this.financialEntityRepository = financialEntityRepository;
        this.cardFinancialRepository = cardFinancialRepository;
        this.moneyRepository = moneyRepository;
    }

    public DataListResponse<BankMovementResponse> list(Long userAuthId) {
        List<BankMovement> bankMovements = bankMovementRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);
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

        List<Account> accountList = BankMovementMapper.receiveToAccount(request, accounts, entrances);

        List<CardFinancialEntity> cardFinancialEntityList = BankMovementMapper.receiveToFinancial(request, cardFinancialEntity, entrances);

        List<Money> moneyList = BankMovementMapper.receiveToMoney(request, entrances, userAuthId, money);

        List<BankMovement> bankMovements = BankMovementMapper.receiveToBankMovement(request, entrances, userAuthId, accounts, cardFinancialEntity, moneyList);

        if (!bankMovements.isEmpty()) {
            bankMovementRepository.saveAll(bankMovements);
        }

        if (!accountList.isEmpty()) {
            accountRepository.saveAll(accountList);
        }

        if (!cardFinancialEntityList.isEmpty()) {
            cardFinancialRepository.saveAll(cardFinancialEntityList);
        }

        if (!moneyList.isEmpty()) {
            moneyRepository.saveAll(moneyList);
        }

        response.setSeverity("success");
        response.setMessage("success");
        return response;
    }
}
