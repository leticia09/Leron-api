package com.leron.api.validator.Goal;

import com.leron.api.model.DTO.goals.GoalsRequest;
import com.leron.api.model.entities.Goals;
import com.leron.api.responses.ApplicationBusinessException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class ValidatorGoal {
    public static void validateCreation(List<GoalsRequest> request, List<Goals> current) throws ApplicationBusinessException {

        AtomicReference<Boolean> nameExists = new AtomicReference<>(false);
        AtomicReference<Boolean> invalidDate = new AtomicReference<>(false);

//        request.forEach(res -> {
//            currentEntrance.forEach(res1 -> {
//
//            });
//        });

        if (invalidDate.get()) {
            throw new ApplicationBusinessException("ERROR", "INVALID_EXPIRATION_DATE");
        }


        if (nameExists.get()) {
            throw new ApplicationBusinessException("ERROR", "NAME_ALREADY_EXISTS");
        }
    }
}
