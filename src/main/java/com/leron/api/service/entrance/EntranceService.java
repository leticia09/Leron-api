package com.leron.api.service.entrance;

import com.leron.api.mapper.entrance.EntranceMapper;
import com.leron.api.model.DTO.entrance.EntranceRequest;
import com.leron.api.model.DTO.entrance.EntranceResponse;
import com.leron.api.model.DTO.graphic.DataSet;
import com.leron.api.model.DTO.graphic.GraphicResponse;
import com.leron.api.model.entities.*;
import com.leron.api.repository.EntranceRepository;
import com.leron.api.repository.MemberRepository;
import com.leron.api.repository.RegisterBankRepository;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataResponse;
import com.leron.api.validator.entrance.ValidatorEntrance;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class EntranceService {

    final EntranceRepository entranceRepository;
    final MemberRepository memberRepository;

    final RegisterBankRepository bankRepository;

    public EntranceService(EntranceRepository entranceRepository, MemberRepository memberRepository, RegisterBankRepository bankRepository) {
        this.entranceRepository = entranceRepository;
        this.memberRepository = memberRepository;
        this.bankRepository = bankRepository;
    }

    public DataResponse<EntranceResponse> create(List<EntranceRequest> requestDTO, String locale, String authorization) throws ApplicationBusinessException {
        DataResponse<EntranceResponse> response = new DataResponse<>();

        List<Entrance> entrances = entranceRepository.findAllByUserAuthIdAndDeletedFalse(requestDTO.get(0).getUserAuthId());

        ValidatorEntrance.validateCreation(requestDTO, entrances);

        entranceRepository.saveAll(EntranceMapper.requestToEntity(requestDTO));

        response.setSeverity("success");
        response.setMessage("success");

        return response;
    }

    public DataResponse<GraphicResponse> getData(Long authId) {
        DataResponse<GraphicResponse> response = new DataResponse<>();

        List<Member> members = memberRepository.findAllByUserAuthIdAndDeletedFalseAndStatusOrderByNameAsc(authId, "ACTIVE");
        List<Entrance> entrances = entranceRepository.findAllByUserAuthIdAndDeletedFalse(authId);

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

            for (Entrance entrance : entrances) {
                if (member.getId().equals(entrance.getOwnerId())) {
                    int labelIndex = labels.indexOf(entrance.getType());
                    if (labelIndex == -1) {
                        labels.add(entrance.getType());
                        //quanto eu recebi, caso não tenha recebido, quanto está previso
                        data.add(entrance.getSalary());
                    } else {
                        data.set(labelIndex, data.get(labelIndex).add(entrance.getSalary()));
                    }
                        //Recebido Anual, Recebido Mensal, Quantidade Confirmado, Quantidade Pendente

//                    if (account.getCurrency().equalsIgnoreCase("Real (R$)")) {
//                        totalMoney = totalMoney.add(account.getValue());
//                    }
//
//                    if (account.getCurrency().equalsIgnoreCase("Dólar ($)")) {
//                        totalDollar = totalDollar.add(account.getValue());
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

    public DataListResponse<EntranceResponse> list(Long userAuthId) {
        List<Entrance> entrances = entranceRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);
        List<Member> members = memberRepository.findAllByUserAuthIdAndDeletedFalseOrderByNameAsc(userAuthId);
        List<Bank> banks = bankRepository.findByUserAuthId(userAuthId);
        return EntranceMapper.entityToResponse(entrances, members, banks);
    }
}
