package com.leron.api.mapper.entrance;

import com.leron.api.model.DTO.entrance.EntranceRequest;
import com.leron.api.model.DTO.entrance.EntranceResponse;
import com.leron.api.model.entities.Bank;
import com.leron.api.model.entities.Entrance;
import com.leron.api.model.entities.Member;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.Request;
import jdk.jfr.Frequency;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class EntranceMapper {

    public static List<Entrance> requestToEntity(List<EntranceRequest> requests) {
        List<Entrance> response = new ArrayList<>();

        requests.forEach(request -> {
            Entrance entrance = new Entrance();
            entrance.setAccountNumber(request.getAccountNumber());
            entrance.setBankId(request.getBankId());
            entrance.setSalary(request.getSalary());
            entrance.setType(request.getType());
            entrance.setSource(request.getSource());
            entrance.setOwnerId(request.getOwnerId());
            entrance.setPaymentDone(false);
            entrance.setCreatedIn(new Date());
            entrance.setDeleted(false);
            entrance.setUserAuthId(request.getUserAuthId());
            entrance.setFrequency(request.getFrequency());
            entrance.setStatus("Aguardando");
            if(request.getFrequency().equalsIgnoreCase("ÚNICO")) {
                entrance.setInitialDate(formatDate(request.getInitialDate()));
            } else if(request.getFrequency().equalsIgnoreCase("MENSAL")) {
                entrance.setInitialDate(formatDate(request.getInitialDate()));
                entrance.setDayReceive(request.getDayReceive());
                if(Objects.nonNull(request.getFinalDate())) {
                    entrance.setFinalDate(formatDate(request.getFinalDate()));
                }
            } else {
                entrance.setInitialDate(formatDate(request.getInitialDate()));
                entrance.setInitialDate(formatDate(request.getInitialDate()));
                entrance.setDayReceive(request.getDayReceive());
                entrance.setMonthReceive(request.getMonthReceive());
                if(Objects.nonNull(request.getFinalDate())) {
                    entrance.setFinalDate(formatDate(request.getFinalDate()));
                }
            }

            response.add(entrance);
        });

        return response;
    }

    private static Timestamp formatDate(String date) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDate = LocalDate.parse(date, dateFormatter);
        LocalDateTime localDateTime = localDate.atStartOfDay();
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = localDateTime.format(outputFormatter);

        return Timestamp.valueOf(formattedDateTime);
    }

    public static DataListResponse<EntranceResponse> entityToResponse(List<Entrance> entrances, List<Member> members, List<Bank> banks) {
        DataListResponse<EntranceResponse> response = new DataListResponse<>();
        List<EntranceResponse> entranceList = new ArrayList<>();

        for (Entrance entrance : entrances) {
            EntranceResponse entranceResponse = new EntranceResponse();
            entranceResponse.setId(entrance.getId());
            entranceResponse.setFrequency(entrance.getFrequency());
            entranceResponse.setSalary(entrance.getSalary());
            entranceResponse.setSource(entrance.getSource());
            entranceResponse.setType(entrance.getType());
            entranceResponse.setAccountNumber(entrance.getAccountNumber());
            entranceResponse.setPaymentDone(entrance.getPaymentDone());
            entranceResponse.setStatus(entrance.getStatus());

            if(Objects.nonNull(entrance.getDayReceive())) {
                entranceResponse.setDayReceive(entrance.getDayReceive());
            }

            if(Objects.nonNull(entrance.getMonthReceive())) {
                entranceResponse.setMonthReceive(entrance.getMonthReceive());
            }

            if(Objects.nonNull(entrance.getInitialDate())) {
                entranceResponse.setInitialDate(entrance.getInitialDate());
            }

            if(Objects.nonNull(entrance.getFinalDate())) {
                entranceResponse.setFinalDate(entrance.getFinalDate());
            }


            Optional<Member> ownerMember = members.stream()
                    .filter(member -> member.getId().equals(entrance.getOwnerId()))
                    .findFirst();

            Optional<Bank> bank = banks.stream()
                    .filter(bank1 -> bank1.getId().equals(entrance.getBankId()))
                    .findFirst();

            if(ownerMember.isPresent() && bank.isPresent()) {
                entranceResponse.setOwner(ownerMember.get());
                entranceResponse.setBankName(bank.get().getName());
                entranceList.add(entranceResponse);
            }
        }
        response.setData(entranceList);

        return response;
    }
}
