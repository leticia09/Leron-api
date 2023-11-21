package com.leron.api.service.movementBank;

import com.leron.api.model.DTO.graphic.DataSet;
import com.leron.api.model.DTO.graphic.GraphicResponse;
import com.leron.api.model.entities.Account;
import com.leron.api.model.entities.Bank;
import com.leron.api.model.entities.Member;
import com.leron.api.repository.MemberRepository;
import com.leron.api.repository.RegisterBankRepository;
import com.leron.api.responses.DataResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MovementBankService {
    private final MemberRepository memberRepository;
    private final RegisterBankRepository bankRepository;

    public MovementBankService(MemberRepository memberRepository, RegisterBankRepository bankRepository) {
        this.memberRepository = memberRepository;
        this.bankRepository = bankRepository;
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
}
