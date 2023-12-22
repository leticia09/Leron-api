package com.leron.api.validator;

import com.leron.api.model.DTO.BankMovement.ReceiveRequest;
import com.leron.api.model.entities.BankMovement;
import com.leron.api.model.entities.Entrance;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.utils.GetStatusPayment;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Component
public class BankMovementValidator {
    public static void validate(List<ReceiveRequest> requestList, List<Entrance> entranceList, List<BankMovement> bankMovements)throws ApplicationBusinessException {

        AtomicReference<Boolean> nameExists = new AtomicReference<>(false);
        AtomicReference<Boolean> invalid = new AtomicReference<>(false);

        LocalDate currentDate = LocalDate.now();
        int DAY = currentDate.getDayOfMonth();
        int MONTH = currentDate.getMonthValue();
        int YEAR = currentDate.getYear();

        requestList.forEach(res -> {
            Optional<Entrance> entranceOptional = entranceList.stream().filter(entrance -> entrance.getId().toString().equals(res.getEntrance())).findFirst();
            if(entranceOptional.isPresent()) {
                List<BankMovement> bankMovementList = bankMovements.stream().filter(bm -> Objects.nonNull(bm.getEntranceId()) && bm.getEntranceId().equals(entranceOptional.get().getId())).collect(Collectors.toList());
                if(GetStatusPayment.getStatus(entranceOptional.get(), bankMovementList, MONTH, YEAR).equalsIgnoreCase("Não Iniciada")) {
                    invalid.set(Boolean.TRUE);
                }
            }
        });

        if (invalid.get()) {
            throw new ApplicationBusinessException("ERROR", "ENTRANCE_INVALID");
        }


        if (nameExists.get()) {
            throw new ApplicationBusinessException("ERROR", "NAME_ALREADY_EXISTS");
        }
    }
}
