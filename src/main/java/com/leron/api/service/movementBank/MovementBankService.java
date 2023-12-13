package com.leron.api.service.movementBank;

import com.leron.api.mapper.bankMovement.BankMovementMapper;
import com.leron.api.model.DTO.BankMovement.BankMovementResponse;
import com.leron.api.model.DTO.BankMovement.ReceiveRequest;
import com.leron.api.model.DTO.graphic.DataSet;
import com.leron.api.model.DTO.graphic.GraphicResponse;
import com.leron.api.model.entities.*;
import com.leron.api.repository.*;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataResponse;
import com.leron.api.validator.BankMovementValidator;
import com.leron.api.validator.entrance.ValidatorEntrance;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MovementBankService {
    private final MemberRepository memberRepository;
    private final RegisterBankRepository bankRepository;
    private final EntranceRepository entranceRepository;
    private final BankMovementRepository bankMovementRepository;
    private final AccountRepository accountRepository;

    public MovementBankService(MemberRepository memberRepository, RegisterBankRepository bankRepository, EntranceRepository entranceRepository, BankMovementRepository bankMovementRepository, AccountRepository accountRepository) {
        this.memberRepository = memberRepository;
        this.bankRepository = bankRepository;
        this.entranceRepository = entranceRepository;
        this.bankMovementRepository = bankMovementRepository;
        this.accountRepository = accountRepository;
    }

    public DataListResponse<BankMovementResponse> list(Long userAuthId) {
        List<BankMovement> bankMovements = bankMovementRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);
        return BankMovementMapper.entitiesToResponse(bankMovements);
    }
    public DataResponse<GraphicResponse> getData(Long authId) {
        DataResponse<GraphicResponse> response = new DataResponse<>();

        List<Member> members = memberRepository.findAllByUserAuthIdAndDeletedFalseAndStatusOrderByNameAsc(authId, "ACTIVE");
        List<Bank> banks = bankRepository.findByUserAuthId(authId);

        BigDecimal totalMoney = BigDecimal.ZERO;
        BigDecimal totalAvailable = BigDecimal.ZERO;
        BigDecimal totalGoal = BigDecimal.ZERO;
        BigDecimal totalDollar = BigDecimal.ZERO;

        GraphicResponse graphicResponse = new GraphicResponse();

        ArrayList<DataSet> dataSets = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        for (Member member : members) {
            DataSet dataSet = new DataSet();
            ArrayList<BigDecimal> data = new ArrayList<>(Collections.nCopies(labels.size(), BigDecimal.ZERO));

            for(Bank bank: banks) {
                for(Account account: bank.getAccounts()) {
                    if(member.getId().equals(account.getMemberId())) {
                        int labelIndex = labels.indexOf(bank.getName());
                        if (labelIndex == -1) {
                            labels.add(bank.getName());
                            data.add(account.getValue());
                        } else {
                            data.set(labelIndex, data.get(labelIndex).add(account.getValue()));
                        }

                        if(account.getCurrency().equalsIgnoreCase("Real (R$)")) {
                            totalMoney = totalMoney.add(account.getValue());
                        }

                        if(account.getCurrency().equalsIgnoreCase("Dólar ($)")) {
                            totalDollar = totalDollar.add(account.getValue());
                        }

                    }
                }

            }
            if(!data.isEmpty()) {
                dataSet.setLabel(member.getName());
                dataSet.setBackgroundColor(member.getColor());
                dataSet.setBorderColor(member.getColor());
                dataSet.setData(data);
                dataSets.add(dataSet);
            }

        }

        graphicResponse.setDataSet(dataSets);
        graphicResponse.setLabels(labels);
        graphicResponse.setTotal1(totalMoney);
        graphicResponse.setTotal2(totalAvailable);
        graphicResponse.setTotal3(totalGoal);
        graphicResponse.setTotal4(totalDollar);

        response.setData(graphicResponse);

        return response;
    }

    public DataResponse<BankMovementResponse> createReceive(List<ReceiveRequest> request, Long userAuthId) throws ApplicationBusinessException {
        DataResponse<BankMovementResponse> response = new DataResponse<>();
        List<Entrance> entrances = entranceRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);
        List<BankMovement> bankMovementList = bankMovementRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);
        List<Account> accounts = accountRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);
        List<Account> accountList = BankMovementMapper.receiveToAccount(request, accounts, entrances);
        List<BankMovement> bankMovements = BankMovementMapper.receiveToBankMovement(request, entrances, userAuthId, accounts);

        BankMovementValidator.validate(request, entrances, bankMovementList);


        if(!bankMovements.isEmpty() && !accountList.isEmpty()) {
            bankMovementRepository.saveAll(bankMovements);
            accountRepository.saveAll(accountList);
        }

        response.setSeverity("success");
        response.setMessage("success");
        return response;
    }
}
