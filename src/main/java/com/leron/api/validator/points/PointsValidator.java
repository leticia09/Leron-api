package com.leron.api.validator.points;

import com.leron.api.model.DTO.points.PointsRequest;
import com.leron.api.model.entities.Score;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class PointsValidator {

    public static void validator(List<PointsRequest> request, List<Score> currentPoint) throws ApplicationBusinessException {

        AtomicReference<Boolean> isSameBankName = new AtomicReference<>(false);
        AtomicReference<Boolean> valueISNull = new AtomicReference<>(false);

        currentPoint.forEach(point -> {
            request.forEach(res -> {
                if(point.getProgram().equalsIgnoreCase(res.getProgram())) {
                    isSameBankName.set(true);
                }

                if(Objects.isNull(point.getValue())) {
                    valueISNull.set(true);
                }
            });

        });

        if(isSameBankName.get()){
            throw new ApplicationBusinessException("ERROR", "PROGRAM_ALREADY_EXISTS");
        }

        if(valueISNull.get()){
            throw new ApplicationBusinessException("ERROR", "VALUE_IS_NULL");
        }
    }

}
