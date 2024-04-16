package com.leron.api.validator.entrance;

import com.leron.api.model.DTO.entrance.EntranceRequest;
import com.leron.api.model.entities.Entrance;
import com.leron.api.responses.ApplicationBusinessException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class ValidatorEntrance {
    public static void validateCreation(List<EntranceRequest> request, List<Entrance> currentEntrance) throws ApplicationBusinessException {

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
