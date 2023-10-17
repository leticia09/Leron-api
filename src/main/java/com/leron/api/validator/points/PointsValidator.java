package com.leron.api.validator.points;

import com.leron.api.model.DTO.points.PointsRequest;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataRequest;
import org.springframework.stereotype.Component;

@Component
public class PointsValidator {

    public static void validator(DataRequest<PointsRequest> request) throws ApplicationBusinessException {
        if(request.getData().getProgram() == null || request.getData().getProgram().isEmpty()){
            throw new ApplicationBusinessException("Lascou", "PROGRAM_IS_EMPTY");
        }

        if(request.getData().getStatus() == null || request.getData().getStatus().isEmpty()){
            throw new ApplicationBusinessException("Lascou", "STATUS_IS_EMPTY");
        }
    }

}
