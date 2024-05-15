package com.leron.api.mapper.Goals;

import com.leron.api.model.DTO.goals.GoalsRequest;
import com.leron.api.model.DTO.goals.GoalsResponse;
import com.leron.api.model.entities.*;
import com.leron.api.utils.FormatDate;
import com.leron.api.utils.GetStatusPayment;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class GoalsMapper {
    public static List<Goals> requestToEntity(List<GoalsRequest> requests) {
        List<Goals> response = new ArrayList<>();

        requests.forEach(request -> {
            Goals goal = new Goals();

            goal.setGoal(request.getGoal());
            goal.setOwnerId(request.getOwnerId());
            goal.setAccountId(Long.valueOf(request.getAccountNumber()));
            goal.setBankId(request.getBankId());
            goal.setOpenDate(FormatDate.formatDate(request.getOpenDate()));
            goal.setValidityInMonths(request.getValidityInMonths());
            goal.setCurrency(request.getCurrency());
            goal.setProfitabilityMonthly(request.getProfitabilityMonthly());
            goal.setGoalPreference(request.getGoalPreference());
            goal.setFees(new BigDecimal(request.getFees().replace(",",".")));
            goal.setContributionTotal(new BigDecimal(request.getContributionTotal().replace(",",".")));

            if (Objects.nonNull(request.getValue())) {
                goal.setValue(new BigDecimal(request.getValue().replace(",",".")));
            }

            if (Objects.nonNull(request.getValue())) {
                goal.setPartValue(new BigDecimal(request.getPartValue().replace(",",".")));
            }

            goal.setUserAuthId(request.getUserAuthId());
            goal.setCreatedIn(new Date());
            goal.setDeleted(false);
            response.add(goal);
        });

        return response;
    }

    public static List<GoalsResponse> entityToResponse(List<Goals> goalsListEntity, List<Member> members, List<Bank> banks, List<BankMovement> bankMovements, int month, int year) {
        List<GoalsResponse> goalsList = new ArrayList<>();
        for (Goals goals : goalsListEntity) {
            String monthValidate = "" + month;
            if (month < 10) {
                monthValidate = "0" + month;
            }
            String period = monthValidate + "/" + year;

            GoalsResponse goalsResponse = new GoalsResponse();
            goalsResponse.setId(goals.getId());
            goalsResponse.setGoal(goals.getGoal());
            goalsResponse.setAccountNumber(String.valueOf(goals.getAccountId()));
            goalsResponse.setFees(goals.getFees());
            goalsResponse.setValue(goals.getValue());
            goalsResponse.setGoalPreference(goals.getGoalPreference());
            goalsResponse.setContributionTotal(goals.getContributionTotal());
            goalsResponse.setBankId(goals.getBankId());
            goalsResponse.setCurrency(goals.getCurrency());
            goalsResponse.setPartValue(goals.getPartValue());
            goalsResponse.setProfitabilityMonthly(goals.getProfitabilityMonthly());
            goalsResponse.setValidityInMonths(goals.getValidityInMonths());
            goalsResponse.setOwnerId(goals.getOwnerId());

            List<BankMovement> bankMovementList = bankMovements.stream()
                    .filter(bm -> Objects.equals(bm.getGoalId(), goals.getId()) &&
                            bm.getReferencePeriod().equalsIgnoreCase(period)).collect(Collectors.toList());


            String status = GetStatusPayment.getStatus(goals, bankMovementList, month, year);

            final BigDecimal[] valueReceived = {BigDecimal.ZERO};

            if (status.equalsIgnoreCase("Não Iniciada")) {
                goalsResponse.setStatus("Não Iniciada");
            }

            if (status.equalsIgnoreCase("Aguardando")) {
                goalsResponse.setStatus("Aguardando");
            }

            if (status.equalsIgnoreCase("Pendente")) {
                goalsResponse.setStatus("Pendente");
            }
            if (status.equalsIgnoreCase("Confirmado")) {
                goalsResponse.setStatus("Confirmado");

                Optional<BankMovement> bankMovement = bankMovementList.stream().filter(bank -> bank.getReferencePeriod().equalsIgnoreCase(period) && bank.getEntranceId().equals(goals.getId())).findFirst();
                bankMovement.ifPresent(movement -> valueReceived[0] = valueReceived[0].add(movement.getValue()));
                goalsResponse.setContributionValuePaid(valueReceived[0]);
            }


            if (Objects.nonNull(goals.getOpenDate())) {
                goalsResponse.setOpenDate(goals.getOpenDate());
            }



            Optional<Member> ownerMember = members.stream()
                    .filter(member -> member.getId().equals(goals.getOwnerId()))
                    .findFirst();

            Optional<Bank> bank = banks.stream()
                    .filter(bank1 -> bank1.getId().equals(goals.getBankId()))
                    .findFirst();


            if (ownerMember.isPresent()) {

                if (bank.isPresent()) {
                    Optional<Account> account = bank.get().getAccounts().stream()
                            .filter(account1 -> account1.getId().toString().equals(goals.getAccountId().toString()))
                            .findFirst();
                    account.ifPresent(value -> goalsResponse.setCurrency(value.getCurrency()));
                    goalsResponse.setBankName(bank.get().getName());
                }

                goalsResponse.setOwnerId(ownerMember.get().getId());

                goalsList.add(goalsResponse);
            }
        }
        return goalsList.stream().filter(goalsResponse -> Objects.nonNull(goalsResponse.getStatus()) &&
                !goalsResponse.getStatus().equalsIgnoreCase("Não Iniciada")).collect(Collectors.toList());

    }


}
