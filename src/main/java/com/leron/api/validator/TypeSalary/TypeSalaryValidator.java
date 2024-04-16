package com.leron.api.validator.TypeSalary;

import com.leron.api.model.DTO.points.PointsRequest;
import com.leron.api.model.DTO.typeSalary.TypeSalaryRequest;
import com.leron.api.model.entities.Score;
import com.leron.api.model.entities.TypeSalary;
import com.leron.api.responses.ApplicationBusinessException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class TypeSalaryValidator {
    public static void validator(List<TypeSalaryRequest> request) throws ApplicationBusinessException {
        AtomicReference<Boolean> isSameBankName = new AtomicReference<>(false);


        if (request.stream().anyMatch
                (type -> request.stream().anyMatch
                        (currentType -> currentType.getDescription().equalsIgnoreCase(type.getDescription()))
                )
        ) {
            isSameBankName.set(Boolean.TRUE);
        }

        if (isSameBankName.get()) {
            throw new ApplicationBusinessException("ERROR", "NAME_ALREADY_EXISTS");
        }

    }
}
