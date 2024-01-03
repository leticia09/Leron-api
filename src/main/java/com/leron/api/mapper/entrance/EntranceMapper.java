package com.leron.api.mapper.entrance;

import com.leron.api.model.DTO.entrance.EntranceRequest;
import com.leron.api.model.DTO.entrance.EntranceResponse;
import com.leron.api.model.entities.*;
import com.leron.api.responses.DataListResponse;
import com.leron.api.utils.FormatDate;
import com.leron.api.utils.GetStatusPayment;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class EntranceMapper {

    public static List<Entrance> requestToEntity(List<EntranceRequest> requests) {
        List<Entrance> response = new ArrayList<>();

        requests.forEach(request -> {
            Entrance entrance = new Entrance();

            entrance.setSalary(new BigDecimal(request.getSalary().replace(",", ".")));
            entrance.setType(request.getType());
            entrance.setSource(request.getSource());
            entrance.setOwnerId(request.getOwnerId());
            entrance.setCreatedIn(new Date());
            entrance.setDeleted(false);
            entrance.setUserAuthId(request.getUserAuthId());
            entrance.setFrequency(request.getFrequency());
            if (Objects.nonNull(request.getMoneyId())) {
                entrance.setMoneyId(request.getMoneyId());
            }
            if (Objects.nonNull(request.getCardId())) {
                entrance.setFinancialEntityCardId(request.getCardId());
                entrance.setFinancialEntityId(request.getTicketId());
            }
            if (Objects.nonNull(request.getAccountNumber())) {
                entrance.setAccountId(Long.valueOf(request.getAccountNumber()));
            }
            if (Objects.nonNull(request.getBankId())) {
                entrance.setBankId(request.getBankId());
            }
            if (request.getFrequency().equalsIgnoreCase("ÚNICO")) {
                entrance.setInitialDate(FormatDate.formatDate(request.getInitialDate()));
            } else if (request.getFrequency().equalsIgnoreCase("MENSAL")) {
                entrance.setInitialDate(FormatDate.formatDate(request.getInitialDate()));
                entrance.setDayReceive(request.getDayReceive());
                if (Objects.nonNull(request.getFinalDate())) {
                    entrance.setFinalDate(FormatDate.formatDate(request.getFinalDate()));
                }
            } else {
                entrance.setInitialDate(FormatDate.formatDate(request.getInitialDate()));
                entrance.setInitialDate(FormatDate.formatDate(request.getInitialDate()));
                entrance.setDayReceive(request.getDayReceive());
                entrance.setMonthReceive(request.getMonthReceive());
                if (Objects.nonNull(request.getFinalDate())) {
                    entrance.setFinalDate(FormatDate.formatDate(request.getFinalDate()));
                }
            }

            response.add(entrance);
        });

        return response;
    }


    public static DataListResponse<EntranceResponse> entityToResponse(List<Money> moneyList, List<CardFinancialEntity> cardFinancial, List<Entrance> entrances, List<Member> members, List<Bank> banks, List<BankMovement> bankMovements, int month, int year) {
        DataListResponse<EntranceResponse> response = new DataListResponse<>();
        List<EntranceResponse> entranceList = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        int DAY = currentDate.getDayOfMonth();
        int MONTH = currentDate.getMonthValue();
        int YEAR = currentDate.getYear();
        AtomicInteger movementMonth = new AtomicInteger();
        AtomicInteger movementYear = new AtomicInteger();

        for (Entrance entrance : entrances) {
            EntranceResponse entranceResponse = new EntranceResponse();
            entranceResponse.setId(entrance.getId());
            entranceResponse.setFrequency(entrance.getFrequency());
            entranceResponse.setSalary(entrance.getSalary());
            entranceResponse.setSource(entrance.getSource());
            entranceResponse.setType(entrance.getType());
            entranceResponse.setAccountNumber(String.valueOf(entrance.getAccountId()));

            List<BankMovement> bankMovementList = bankMovements.stream()
                    .filter(bm -> Objects.equals(bm.getEntranceId(), entrance.getId())).collect(Collectors.toList());


            String status = GetStatusPayment.getStatus(entrance, bankMovementList, month, year);

            final BigDecimal[] valueReceived = {BigDecimal.ZERO};

            if (status.equalsIgnoreCase("Não Iniciada")) {
                entranceResponse.setStatus("Não Iniciada");
            }

            if (status.equalsIgnoreCase("Aguardando")) {
                entranceResponse.setStatus("Aguardando");
            }

            if (status.equalsIgnoreCase("Pendente")) {
                entranceResponse.setStatus("Pendente");
            }
            if(status.equalsIgnoreCase("Confirmado")) {
                entranceResponse.setStatus("Confirmado");
                Optional<BankMovement> bankMovement = bankMovementList.stream().filter(bank -> bank.getReferencePeriod().equalsIgnoreCase(month+"/"+year) && bank.getEntranceId().equals(entrance.getId())).findFirst();
                bankMovement.ifPresent(movement -> valueReceived[0] = valueReceived[0].add(movement.getValue()));
                entranceResponse.setValueReceived(valueReceived[0]);
            }

            if (Objects.nonNull(entrance.getDayReceive())) {
                entranceResponse.setDayReceive(entrance.getDayReceive());
            }

            if (Objects.nonNull(entrance.getMonthReceive())) {
                entranceResponse.setMonthReceive(entrance.getMonthReceive());
            }

            if (Objects.nonNull(entrance.getInitialDate())) {
                entranceResponse.setInitialDate(entrance.getInitialDate());
            }

            if (Objects.nonNull(entrance.getFinalDate())) {
                entranceResponse.setFinalDate(entrance.getFinalDate());
            }


            Optional<Member> ownerMember = members.stream()
                    .filter(member -> member.getId().equals(entrance.getOwnerId()))
                    .findFirst();

            Optional<Bank> bank = banks.stream()
                    .filter(bank1 -> bank1.getId().equals(entrance.getBankId()))
                    .findFirst();

            Optional<CardFinancialEntity> card = cardFinancial.stream().filter(c -> c.getId().equals(entrance.getFinancialEntityCardId())).findFirst();
            Optional<Money> money = moneyList.stream().filter(m -> m.getId().equals(entrance.getMoneyId())).findFirst();

            if (ownerMember.isPresent()) {

                if (bank.isPresent()) {
                    Optional<Account> account = bank.get().getAccounts().stream()
                            .filter(account1 -> account1.getId().toString().equals(entrance.getAccountId().toString()))
                            .findFirst();
                    account.ifPresent(value -> entranceResponse.setCurrency(value.getCurrency()));
                    entranceResponse.setBankName(bank.get().getName());
                }

                card.ifPresent(cardFinancialEntity -> entranceResponse.setFinancialCardName(cardFinancialEntity.getCardName()));
                card.ifPresent(cardFinancialEntity -> entranceResponse.setCurrency(cardFinancialEntity.getCurrency()));
                money.ifPresent(mo -> entranceResponse.setCurrency(mo.getCurrency()));
                entranceResponse.setOwner(ownerMember.get());

                entranceList.add(entranceResponse);
            }
        }
        response.setData(entranceList.stream().filter(entranceResponse -> Objects.nonNull(entranceResponse.getStatus()) && !entranceResponse.getStatus().equalsIgnoreCase("Não Iniciada")).collect(Collectors.toList()));

        return response;
    }

    public static DataListResponse<EntranceResponse> entityToResponse(List<Money> moneyList, List<CardFinancialEntity> cardFinancial, List<Entrance> entrances, List<Member> members, List<Bank> banks) {
        DataListResponse<EntranceResponse> response = new DataListResponse<>();
        List<EntranceResponse> entranceList = new ArrayList<>();
        for (Entrance entrance : entrances) {
            EntranceResponse entranceResponse = new EntranceResponse();
            entranceResponse.setId(entrance.getId());
            entranceResponse.setFrequency(entrance.getFrequency());
            entranceResponse.setSalary(entrance.getSalary());
            entranceResponse.setSource(entrance.getSource());
            entranceResponse.setType(entrance.getType());
            entranceResponse.setAccountNumber(String.valueOf(entrance.getAccountId()));


            if (Objects.nonNull(entrance.getDayReceive())) {
                entranceResponse.setDayReceive(entrance.getDayReceive());
            }

            if (Objects.nonNull(entrance.getMonthReceive())) {
                entranceResponse.setMonthReceive(entrance.getMonthReceive());
            }

            if (Objects.nonNull(entrance.getInitialDate())) {
                entranceResponse.setInitialDate(entrance.getInitialDate());
            }

            if (Objects.nonNull(entrance.getFinalDate())) {
                entranceResponse.setFinalDate(entrance.getFinalDate());
            }

            Optional<Member> ownerMember = members.stream()
                    .filter(member -> member.getId().equals(entrance.getOwnerId()))
                    .findFirst();

            Optional<Bank> bank = banks.stream()
                    .filter(bank1 -> bank1.getId().equals(entrance.getBankId()))
                    .findFirst();

            Optional<CardFinancialEntity> card = cardFinancial.stream().filter(c -> c.getId().equals(entrance.getFinancialEntityCardId())).findFirst();
            Optional<Money> money = moneyList.stream().filter(m -> m.getId().equals(entrance.getMoneyId())).findFirst();


            if (ownerMember.isPresent()) {
                if (bank.isPresent()) {
                    Optional<Account> account = bank.get().getAccounts().stream()
                            .filter(account1 -> account1.getId().toString().equals(entrance.getAccountId().toString()))
                            .findFirst();
                    account.ifPresent(value -> entranceResponse.setCurrency(value.getCurrency()));
                    entranceResponse.setBankName(bank.get().getName());
                }

                card.ifPresent(cardFinancialEntity -> entranceResponse.setFinancialCardName(cardFinancialEntity.getCardName()));
                card.ifPresent(cardFinancialEntity -> entranceResponse.setCurrency(cardFinancialEntity.getCurrency()));
                money.ifPresent(mo -> entranceResponse.setCurrency(mo.getCurrency()));

                entranceResponse.setOwner(ownerMember.get());
                entranceList.add(entranceResponse);
            }
        }
        response.setData(entranceList);
        return response;
    }

    private static ArrayList<ArrayList<Integer>> getArrayLists() {
        ArrayList<ArrayList<Integer>> quarters = new ArrayList<>();
        ArrayList<Integer> quarter1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        ArrayList<Integer> quarter2 = new ArrayList<>(Arrays.asList(5, 6, 7, 8));
        ArrayList<Integer> quarter3 = new ArrayList<>(Arrays.asList(9, 10, 11, 12));
        quarters.add(quarter1);
        quarters.add(quarter2);
        quarters.add(quarter3);
        return quarters;
    }

    private static ArrayList<ArrayList<Integer>> getArrayListsSemester() {
        ArrayList<ArrayList<Integer>> quarters = new ArrayList<>();
        ArrayList<Integer> quarter1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));
        ArrayList<Integer> quarter2 = new ArrayList<>(Arrays.asList(7, 8, 9, 10, 11, 12));
        quarters.add(quarter1);
        quarters.add(quarter2);

        return quarters;
    }

    private static boolean belongsToSameQuarter(int a, int b, ArrayList<ArrayList<Integer>> quarters) {
        for (ArrayList<Integer> quarter : quarters) {
            if (quarter.contains(a) && quarter.contains(b)) {
                return true;
            }
        }
        return false;
    }


}
