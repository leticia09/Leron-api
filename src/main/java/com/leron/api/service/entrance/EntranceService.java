package com.leron.api.service.entrance;

import com.leron.api.mapper.entrance.EntranceMapper;
import com.leron.api.model.DTO.entrance.EntranceManagementResponse;
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
import java.util.stream.Collectors;

import static com.leron.api.utils.FormatDate.populateMonths;

@Service
public class EntranceService {

    final EntranceRepository entranceRepository;

    final TypeSalaryRepository typeSalaryRepository;

    final MemberRepository memberRepository;

    final RegisterBankRepository bankRepository;

    final BankMovementRepository bankMovementRepository;

    final AccountRepository accountRepository;

    final FinancialEntityRepository financialEntityRepository;

    final CardFinancialEntityRepository cardFinancialEntityRepository;

    final MoneyRepository moneyRepository;

    public EntranceService(EntranceRepository entranceRepository, TypeSalaryRepository typeSalaryRepository, MemberRepository memberRepository, RegisterBankRepository bankRepository, BankMovementRepository bankMovementRepository, AccountRepository accountRepository, FinancialEntityRepository financialEntityRepository, CardFinancialEntityRepository cardFinancialEntityRepository, MoneyRepository moneyRepository) {
        this.entranceRepository = entranceRepository;
        this.typeSalaryRepository = typeSalaryRepository;
        this.memberRepository = memberRepository;
        this.bankRepository = bankRepository;
        this.bankMovementRepository = bankMovementRepository;
        this.accountRepository = accountRepository;
        this.financialEntityRepository = financialEntityRepository;
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

    public DataResponse<EntranceManagementResponse> getManagementData(Long authId, int month, int year, List<Long> owners) {
        DataResponse<EntranceManagementResponse> response = new DataResponse<>();
        EntranceManagementResponse entranceManagementResponse = new EntranceManagementResponse();

        entranceManagementResponse.setEntranceResponseList(list(authId, month, year, owners));
        entranceManagementResponse.setGraphicResponseData(getData(authId, month, year, owners));
        entranceManagementResponse.setGraphicResponseDetails(getDataDetails(authId, month, year, owners));

        response.setData(entranceManagementResponse);
        return response;
    }

    public GraphicResponse getData(Long authId, int month, int year, List<Long> owners) {
        List<Member> members = memberRepository.findMemberByIdsAndUserAuthId(authId, owners);
        List<EntranceResponse> entrances = list(authId, month, year, owners);

        BigDecimal receiveTotal = BigDecimal.ZERO;
        BigDecimal receiveOk = BigDecimal.ZERO;
        BigDecimal receiveHoldOn = BigDecimal.ZERO;
        BigDecimal receiveNotOk = BigDecimal.ZERO;

        GraphicResponse graphicResponse = new GraphicResponse();

        ArrayList<DataSet> dataSets = new ArrayList<>();
        ArrayList<String> labels = populateMonths();

        for (Member member : members) {

            dataSets.add(populateEntrances(authId, year, member));


            for (EntranceResponse entrance : entrances) {
                if (member.getId().equals(entrance.getOwnerId())) {

                    if (!entrance.getStatus().equalsIgnoreCase("Não Iniciada") && !entrance.getStatus().isEmpty()) {
                        if (entrance.getStatus().equalsIgnoreCase("Confirmado")) {
                            receiveOk = receiveOk.add(entrance.getValueReceived());
                        }
                    }

                    if (!entrance.getStatus().equalsIgnoreCase("Não Iniciada")) {
                        receiveTotal = receiveTotal.add(entrance.getSalary());
                    }

                    if (entrance.getStatus().equalsIgnoreCase("Aguardando")) {
                        receiveHoldOn = receiveHoldOn.add(entrance.getSalary());
                    }

                    if (entrance.getStatus().equalsIgnoreCase("Pendente")) {
                        receiveNotOk = receiveNotOk.add(entrance.getSalary());
                    }
                }
            }


        }

        graphicResponse.setDataSet(dataSets);
        graphicResponse.setLabels(labels);
        graphicResponse.setTotal1(receiveTotal);
        graphicResponse.setTotal2(receiveOk);
        graphicResponse.setTotal3(receiveHoldOn);
        graphicResponse.setTotal4(receiveNotOk);

        return graphicResponse;
    }

    public GraphicResponse getDataDetails(Long authId, int month, int year, List<Long> owners) {
        List<Member> members = memberRepository.findMemberByIdsAndUserAuthId(authId, owners);
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
                String monthValidate = "" + month;
                if (month < 10) {
                    monthValidate = "0" + month;
                }
                String period = monthValidate + "/" + year;

                List<BankMovement> bankMovementList = bankMovements.stream()
                        .filter(bm -> Objects.equals(bm.getEntranceId(), entrance.getId()) && bm.getReferencePeriod().equalsIgnoreCase(period)).collect(Collectors.toList());

                BigDecimal value = bankMovementList.stream().map(BankMovement::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);

                if (member.getId().equals(entrance.getOwnerId())) {
                    int labelIndex = labels.indexOf(entrance.getType());
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
                    if (status.equalsIgnoreCase("Confirmado")) {
                        receiveOk = receiveOk.add(value);
                    }

                }

            }

            if (!data.isEmpty()) {
                dataSet.setLabel(member.getName());
                dataSet.setBackgroundColor(member.getColor());
                dataSet.setBorderColor(member.getColor());
                dataSet.setFill(true);
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

        return graphicResponse;
    }

    public List<EntranceResponse> list(Long userAuthId, int month, int year, List<Long> owners) {
        List<Entrance> entrances = entranceRepository.findAllByUserAuthIdAndDeletedFalseOrderByInitialDateDesc(userAuthId);
        List<Member> members = memberRepository.findMemberByIdsAndUserAuthId(userAuthId, owners);
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

    public DataResponse<EntranceResponse> getById(Long userAuthId, Long id) {
        DataResponse<EntranceResponse> response = new DataResponse<>();
        Optional<Entrance> entrance = entranceRepository.findByUserAuthIdAndId(userAuthId, id);
        if (entrance.isPresent()) {
            Optional<Account> account = accountRepository.findById(entrance.get().getAccountId());
            Optional<Member> member = memberRepository.findById(entrance.get().getOwnerId());
            response.setData(EntranceMapper.entityToResponse(entrance.get(), member, account));
        }

        return response;
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

    public DataSet populateEntrances(Long userAuthId, int year, Member owner) {
        DataSet dataSet = new DataSet();
        ArrayList<BigDecimal> data = new ArrayList<>();
        List<Long> ownersIds = new ArrayList<>();
        ownersIds.add(owner.getId());

        for (int i = 1; i <= 12; i++) {
            List<EntranceResponse> entrances = list(userAuthId, i, year, ownersIds);
            List<EntranceResponse> entranceFilteredReceived = entrances.stream().filter(entrance ->
                    entrance.getStatus().equalsIgnoreCase("confirmado")
            ).collect(Collectors.toList());

            List<EntranceResponse> entranceFilteredForecast = entrances.stream().filter(entrance ->
                    !entrance.getStatus().equalsIgnoreCase("confirmado")
            ).collect(Collectors.toList());

            BigDecimal totalReceived = entranceFilteredReceived.stream()
                    .map(EntranceResponse::getValueReceived)
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);

            BigDecimal totalForecast = entranceFilteredForecast.stream()
                    .map(EntranceResponse::getSalary)
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);

            data.add(totalReceived.add(totalForecast));
        }

        dataSet.setLabel(owner.getName());
        dataSet.setBackgroundColor(owner.getColor().replace("1)", "0.6)"));
        dataSet.setBorderColor(owner.getColor());
        dataSet.setFill(true);
        dataSet.setData(data);
        return dataSet;
    }

    public DataResponse<EntranceResponse> edit(EntranceResponse entranceResponse) {
        DataResponse<EntranceResponse> response = new DataResponse<>();

        Optional<TypeSalary> typeSalary = typeSalaryRepository.findById(Long.valueOf(entranceResponse.getType()));

        Optional<Entrance> entrance = entranceRepository.findById(entranceResponse.getId());

        if (entrance.isPresent() && typeSalary.isPresent()) {
            Entrance entranceEntity = EntranceMapper.responseToEntity(entranceResponse, entrance.get(), typeSalary.get());
            entranceRepository.save(entranceEntity);
        }

        response.setSeverity("success");
        response.setMessage("success");

        return response;
    }

}
