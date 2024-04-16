package com.leron.api.mapper.Goals;

import com.leron.api.model.DTO.Goals.GoalsRequest;
import com.leron.api.model.entities.Goals;
import com.leron.api.utils.FormatDate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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

}
