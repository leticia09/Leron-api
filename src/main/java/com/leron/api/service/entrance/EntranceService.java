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
import java.math.BigInteger;
import java.time.LocalDate;
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

    public DataResponse<GraphicResponse> getData(Long authId, int month, int year, List<Long> owners) {
        DataResponse<GraphicResponse> response = new DataResponse<>();

        List<Member> members = memberRepository.findMemberByIdsAndUserAuthId(authId, owners);
        List<Entrance> entrances = entranceRepository.findAllByUserAuthIdAndDeletedFalse(authId);
        List<BankMovement> bankMovements = bankMovementRepository.findAllByUserAuthIdAndDeletedFalse(authId);

        BigDecimal receiveTotal = BigDecimal.ZERO;
        BigDecimal receiveOk = BigDecimal.ZERO;
        BigDecimal receiveHoldOn = BigDecimal.ZERO;
        BigDecimal receiveNotOk = BigDecimal.ZERO;

        GraphicResponse graphicResponse = new GraphicResponse();

        ArrayList<DataSet> dataSets = new ArrayList<>();
        ArrayList<String> labels = populateMonths();

        for (Member member : members) {

            dataSets.add(populateEntrances(entrances, month, year, member));

            for (int i = 0; i < 12; i++) {
                for (Entrance entrance : entrances) {
                    int monthValue = i + 1;

                    if (member.getId().equals(entrance.getOwnerId())) {
                        String monthValidate = (monthValue < 10) ? "0" + monthValue : "" + monthValue;
                        String period = monthValidate + "/" + year;

                        List<BankMovement> bankMovementList = bankMovements.stream().filter(bm -> Objects.equals(bm.getEntranceId(), entrance.getId()) && bm.getReferencePeriod().equalsIgnoreCase(period)).collect(Collectors.toList());

                        BigDecimal value = bankMovementList.stream().map(BankMovement::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);

                        String status = GetStatusPayment.getStatus(entrance, bankMovementList, month, year);
                        if (!status.equalsIgnoreCase("Não Iniciada") && !status.isEmpty()) {
                            if (status.equalsIgnoreCase("Confirmado")) {
                                receiveOk = receiveOk.add(value);
                            }

                        }

                        if (!status.equalsIgnoreCase("Não Iniciada") && month == monthValue) {
                            receiveTotal = receiveTotal.add(entrance.getSalary());
                        }

                        if (status.equalsIgnoreCase("Aguardando") && month == monthValue) {
                            receiveHoldOn = receiveHoldOn.add(entrance.getSalary());
                        }

                        if (status.equalsIgnoreCase("Pendente") && month == monthValue) {
                            receiveNotOk = receiveNotOk.add(entrance.getSalary());
                        }
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

        response.setData(graphicResponse);

        return response;
    }

    public DataResponse<GraphicResponse> getDataDetails(Long authId, int month, int year, List<Long> owners) {
        DataResponse<GraphicResponse> response = new DataResponse<>();

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

        response.setData(graphicResponse);

        return response;
    }

    public DataListResponse<EntranceResponse> list(Long userAuthId, int month, int year, List<Long> owners) {
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

    public static DataSet populateEntrances(List<Entrance> entrances, int month, int year, Member owner) {
        DataSet dataSet = new DataSet();
        ArrayList<BigDecimal> data = new ArrayList<>();

        BigDecimal month1 = new BigDecimal(BigInteger.ZERO);
        BigDecimal month2 = new BigDecimal(BigInteger.ZERO);
        BigDecimal month3 = new BigDecimal(BigInteger.ZERO);
        BigDecimal month4 = new BigDecimal(BigInteger.ZERO);
        BigDecimal month5 = new BigDecimal(BigInteger.ZERO);
        BigDecimal month6 = new BigDecimal(BigInteger.ZERO);
        BigDecimal month7 = new BigDecimal(BigInteger.ZERO);
        BigDecimal month8 = new BigDecimal(BigInteger.ZERO);
        BigDecimal month9 = new BigDecimal(BigInteger.ZERO);
        BigDecimal month10 = new BigDecimal(BigInteger.ZERO);
        BigDecimal month11 = new BigDecimal(BigInteger.ZERO);
        BigDecimal month12 = new BigDecimal(BigInteger.ZERO);

        for (Entrance entrance : entrances) {
            LocalDate initialDate = entrance.getInitialDate().toLocalDateTime().toLocalDate();
            int initialMonth = initialDate.getMonthValue();
            int initialYear = initialDate.getYear();
            if (entrance.getFrequency().equalsIgnoreCase("mensal") && owner.getId().equals(entrance.getOwnerId())) {
                if (initialMonth <= 1 || initialYear < year) {
                    month1 = month1.add(entrance.getSalary());
                }
                if (initialMonth <= 2 || initialYear < year) {
                    month2 = month2.add(entrance.getSalary());
                }
                if (initialMonth <= 3 || initialYear < year) {
                    month3 = month3.add(entrance.getSalary());
                }
                if (initialMonth <= 4 || initialYear < year) {
                    month4 = month4.add(entrance.getSalary());
                }
                if (initialMonth <= 5 || initialYear < year) {
                    month5 = month5.add(entrance.getSalary());
                }
                if (initialMonth <= 6 || initialYear < year) {
                    month6 = month6.add(entrance.getSalary());
                }
                if (initialMonth <= 7 || initialYear < year) {
                    month7 = month7.add(entrance.getSalary());
                }
                if (initialMonth <= 8 || initialYear < year) {
                    month8 = month8.add(entrance.getSalary());
                }
                if (initialMonth <= 9 || initialYear < year) {
                    month9 = month9.add(entrance.getSalary());
                }
                if (initialMonth <= 10 || initialYear < year) {
                    month10 = month10.add(entrance.getSalary());
                }
                if (initialMonth <= 11 || initialYear < year) {
                    month11 = month11.add(entrance.getSalary());
                }
                if (initialMonth <= 12 || initialYear < year) {
                    month12 = month12.add(entrance.getSalary());
                }

            } else {
                if (entrance.getFrequency().equalsIgnoreCase("anual") && owner.getId().equals(entrance.getOwnerId()) && initialMonth <= month && year == initialYear) {
                    if (entrance.getMonthReceive() == 1) {
                        month1 = month1.add(entrance.getSalary());
                    } else if (entrance.getMonthReceive() == 2) {
                        month2 = month2.add(entrance.getSalary());
                    } else if (entrance.getMonthReceive() == 3) {
                        month3 = month3.add(entrance.getSalary());
                    } else if (entrance.getMonthReceive() == 4) {
                        month4 = month4.add(entrance.getSalary());
                    } else if (entrance.getMonthReceive() == 5) {
                        month5 = month5.add(entrance.getSalary());
                    } else if (entrance.getMonthReceive() == 6) {
                        month6 = month6.add(entrance.getSalary());
                    } else if (entrance.getMonthReceive() == 7) {
                        month7 = month7.add(entrance.getSalary());
                    } else if (entrance.getMonthReceive() == 8) {
                        month8 = month8.add(entrance.getSalary());
                    } else if (entrance.getMonthReceive() == 9) {
                        month9 = month9.add(entrance.getSalary());
                    } else if (entrance.getMonthReceive() == 10) {
                        month10 = month10.add(entrance.getSalary());
                    } else if (entrance.getMonthReceive() == 11) {
                        month11 = month11.add(entrance.getSalary());
                    } else if (entrance.getMonthReceive() == 12) {
                        month12 = month12.add(entrance.getSalary());
                    }

                } else if (entrance.getFrequency().equalsIgnoreCase("única") && owner.getId().equals(entrance.getOwnerId()) && initialMonth <= month) {
                    if (initialMonth == 1 && initialYear == year) {
                        month1 = month1.add(entrance.getSalary());
                    } else if (initialMonth == 2 && initialYear == year) {
                        month2 = month2.add(entrance.getSalary());
                    } else if (initialMonth == 3 && initialYear == year) {
                        month3 = month3.add(entrance.getSalary());
                    } else if (initialMonth == 4 && initialYear == year) {
                        month4 = month4.add(entrance.getSalary());
                    } else if (initialMonth == 5 && initialYear == year) {
                        month5 = month5.add(entrance.getSalary());
                    } else if (initialMonth == 6 && initialYear == year) {
                        month6 = month6.add(entrance.getSalary());
                    } else if (initialMonth == 7 && initialYear == year) {
                        month7 = month7.add(entrance.getSalary());
                    } else if (initialMonth == 8 && initialYear == year) {
                        month8 = month8.add(entrance.getSalary());
                    } else if (initialMonth == 9 && initialYear == year) {
                        month9 = month9.add(entrance.getSalary());
                    } else if (initialMonth == 10 && initialYear == year) {
                        month10 = month10.add(entrance.getSalary());
                    } else if (initialMonth == 11 && initialYear == year) {
                        month11 = month11.add(entrance.getSalary());
                    } else if (initialMonth == 12 && initialYear == year) {
                        month12 = month12.add(entrance.getSalary());
                    }
                }
            }
        }
        data.add(month1);
        data.add(month2);
        data.add(month3);
        data.add(month4);
        data.add(month5);
        data.add(month6);
        data.add(month7);
        data.add(month8);
        data.add(month9);
        data.add(month10);
        data.add(month11);
        data.add(month12);

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
