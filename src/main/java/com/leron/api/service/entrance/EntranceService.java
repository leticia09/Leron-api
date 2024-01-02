package com.leron.api.service.entrance;

import com.leron.api.mapper.entrance.EntranceMapper;
import com.leron.api.model.DTO.entrance.EntranceRequest;
import com.leron.api.model.DTO.entrance.EntranceResponse;
import com.leron.api.model.DTO.graphic.DataSet;
import com.leron.api.model.DTO.graphic.GraphicResponse;
import com.leron.api.model.entities.*;
import com.leron.api.repository.*;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataResponse;
import com.leron.api.utils.GetStatusPayment;
import com.leron.api.validator.entrance.ValidatorEntrance;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class EntranceService {

    final EntranceRepository entranceRepository;
    final MemberRepository memberRepository;

    final RegisterBankRepository bankRepository;

    final BankMovementRepository bankMovementRepository;

    final CardFinancialEntityRepository cardFinancialEntityRepository;

    final MoneyRepository moneyRepository;

    public EntranceService(EntranceRepository entranceRepository, MemberRepository memberRepository, RegisterBankRepository bankRepository, BankMovementRepository bankMovementRepository, CardFinancialEntityRepository cardFinancialEntityRepository, MoneyRepository moneyRepository) {
        this.entranceRepository = entranceRepository;
        this.memberRepository = memberRepository;
        this.bankRepository = bankRepository;
        this.bankMovementRepository = bankMovementRepository;
        this.cardFinancialEntityRepository = cardFinancialEntityRepository;
        this.moneyRepository = moneyRepository;
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

    public DataResponse<GraphicResponse> getData(Long authId, int month, int year) {
        DataResponse<GraphicResponse> response = new DataResponse<>();

        List<Member> members = memberRepository.findAllByUserAuthIdAndDeletedFalseAndStatusOrderByNameAsc(authId, "ACTIVE");
        List<Entrance> entrances = entranceRepository.findAllByUserAuthIdAndDeletedFalse(authId);
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

            for (Entrance entrance : entrances) {
                AtomicInteger movementMonth = new AtomicInteger();
                AtomicInteger movementYear = new AtomicInteger();
                final BigDecimal[] valueReceived = {BigDecimal.ZERO};

                String period = month + "/" + year;

                List<BankMovement> bankMovementList = bankMovements.stream()
                        .filter(bm -> Objects.equals(
                                bm.getEntranceId(), entrance.getId())
                        ).collect(Collectors.toList());


                BigDecimal value = bankMovementList.stream().map(BankMovement::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);

                if (member.getId().equals(entrance.getOwnerId())) {
                    int labelIndex = labels.indexOf(entrance.getType());
                    String a = GetStatusPayment.getStatus(entrance, bankMovementList, month, year);
                    if (labelIndex == -1 && GetStatusPayment.getStatus(entrance, bankMovementList, month, year).equalsIgnoreCase("Confirmado")) {
                        labels.add(entrance.getType());
                        data.add(value);
                    } else {
                        if (GetStatusPayment.getStatus(entrance, bankMovementList, month, year).equalsIgnoreCase("Confirmado")) {
                            data.set(labelIndex, data.get(labelIndex).add(value));
                        }

                    }

                    String status = GetStatusPayment.getStatus(entrance, bankMovementList, month, year);

                    if (!status.equalsIgnoreCase("Não Iniciada")) {
                        receiveTotal = receiveTotal.add(entrance.getSalary());
                    }

                    if (status.equalsIgnoreCase("Aguardando")) {
                        receiveHoldOn = receiveHoldOn.add(entrance.getSalary());
                    }

                    if (status.equalsIgnoreCase("Pendente")) {
                        receiveNotOk = receiveNotOk.add(entrance.getSalary());
                    }
                    if(status.equalsIgnoreCase("Confirmado")) {
                        receiveOk = receiveOk.add(value);
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

        response.setData(graphicResponse);

        return response;
    }

    public DataListResponse<EntranceResponse> list(Long userAuthId, int month, int year) {
        List<Entrance> entrances = entranceRepository.findAllByUserAuthIdAndDeletedFalseOrderByInitialDateDesc(userAuthId);
        List<Member> members = memberRepository.findAllByUserAuthIdAndDeletedFalseOrderByNameAsc(userAuthId);
        List<Bank> banks = bankRepository.findByUserAuthId(userAuthId);
        List<BankMovement> bankMovements = bankMovementRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);
        List<CardFinancialEntity> cardFinancial = cardFinancialEntityRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);
        List<Money> moneyList = moneyRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);
        return EntranceMapper.entityToResponse(moneyList, cardFinancial, entrances, members, banks, bankMovements, month, year);
    }

    public DataListResponse<EntranceResponse> list(Long userAuthId) {
        List<Entrance> entrances = entranceRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);
        List<Member> members = memberRepository.findAllByUserAuthIdAndDeletedFalseOrderByNameAsc(userAuthId);
        List<Bank> banks = bankRepository.findByUserAuthId(userAuthId);
        List<CardFinancialEntity> cardFinancial = cardFinancialEntityRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);
        List<Money> moneyList = moneyRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);
        return EntranceMapper.entityToResponse(moneyList, cardFinancial, entrances, members, banks);
    }

    public DataResponse<EntranceResponse> delete(Long id) {
        DataResponse<EntranceResponse> response = new DataResponse<>();

        Optional<Entrance> entrance = entranceRepository.findById(id);

        if (entrance.isPresent()) {
            entrance.get().setDeleted(true);
            entrance.get().setChangedIn(new Date());

            entranceRepository.save(entrance.get());
        }

        response.setSeverity("success");
        response.setMessage("success");

        return response;
    }

}
