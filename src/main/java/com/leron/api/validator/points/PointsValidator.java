package com.leron.api.validator.points;

import com.leron.api.model.DTO.points.PointsRequest;
import com.leron.api.model.DTO.points.StatusRequest;
import com.leron.api.model.DTO.points.TransferRequest;
import com.leron.api.model.entities.Score;
import com.leron.api.responses.ApplicationBusinessException;
import org.springframework.stereotype.Component;

import javax.print.attribute.standard.JobKOctets;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.sql.Timestamp;

@Component
public class PointsValidator {

    public static void validator(List<PointsRequest> request, List<Score> currentPoint) throws ApplicationBusinessException {

        AtomicReference<Boolean> isSameBankName = new AtomicReference<>(false);
        AtomicReference<Boolean> valueISNull = new AtomicReference<>(false);
        AtomicReference<Boolean> invalidDate = new AtomicReference<>(false);

        currentPoint.forEach(point -> {
            request.forEach(res -> {
                if (point.getProgram().equalsIgnoreCase(res.getProgram())) {
                    isSameBankName.set(true);
                }

                if (Objects.isNull(point.getValue())) {
                    valueISNull.set(true);
                }

                if (Objects.nonNull(res.getPointsExpirationDate()) && isExpirationDateInvalid(res.getPointsExpirationDate())) {
                    invalidDate.set(true);
                }
            });

        });

        if (invalidDate.get()) {
            throw new ApplicationBusinessException("ERROR", "INVALID_EXPIRATION_DATE");
        }

        if (isSameBankName.get()) {
            throw new ApplicationBusinessException("ERROR", "PROGRAM_ALREADY_EXISTS");
        }

        if (valueISNull.get()) {
            throw new ApplicationBusinessException("ERROR", "VALUE_IS_NULL");
        }
    }

    public static void validatorTransfer(TransferRequest request) throws ApplicationBusinessException {
        if (Objects.isNull(request.getOriginProgramId())) {
            throw new ApplicationBusinessException("ERROR", "ORIGIN_PROGRAM_ID_IS_NULL");
        }

        if (Objects.isNull(request.getDestinyProgramId())) {
            throw new ApplicationBusinessException("ERROR", "DESTINY_PROGRAM_ID_IS_NULL");
        }

        if (Objects.isNull(request.getQuantity())) {
            throw new ApplicationBusinessException("ERROR", "INVALID_QUANTITY");
        }

        if (Objects.nonNull(request.getPointsExpirationDate()) && isExpirationDateInvalid(request.getPointsExpirationDate())) {
            throw new ApplicationBusinessException("ERROR", "INVALID_EXPIRATION_DATE");
        }

        if (Objects.isNull(request.getOriginValue())) {
            throw new ApplicationBusinessException("ERROR", "INVALID_ORIGIN_VALUE");
        }

        if (Objects.isNull(request.getDestinyValue())) {
            throw new ApplicationBusinessException("ERROR", "INVALID_DESTINY_VALUE");
        }

    }

    private static boolean isExpirationDateInvalid(Timestamp expirationDate) {
        LocalDate currentDate = LocalDate.now();
        LocalDate expiration = expirationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return expiration.isBefore(currentDate) || expiration.isEqual(currentDate);
    }

    public static void validatorValueTransfer(Score valueOrigin, Score valueDestiny) throws ApplicationBusinessException {
        if (valueOrigin.getValue().compareTo(BigDecimal.ZERO) < 0) {
            throw new ApplicationBusinessException("ERROR", "INSUFFICIENT_ORIGIN_BALANCE");
        }

        if (valueDestiny.getValue().compareTo(BigDecimal.ZERO) < 0) {
            throw new ApplicationBusinessException("ERROR", "INSUFFICIENT_DESTINY_BALANCE");
        }

        if (valueOrigin.getId().equals(valueDestiny.getId()))
            throw new ApplicationBusinessException("ERROR", "PROGRAM_IS_EQUAL");
    }

    public static void validateStatus(StatusRequest request) throws ApplicationBusinessException {
        if (Objects.isNull(request.getStatus())) {
            throw new ApplicationBusinessException("ERROR", "STATUS_IS_EMPTY");
        }

        if (Objects.isNull(request.getProgramId())) {
            throw new ApplicationBusinessException("ERROR", "PROGRAM_IS_EMPTY");

        }
    }

    public static void validateValue(BigDecimal request) throws ApplicationBusinessException {
        if (request.compareTo(BigDecimal.ZERO) < 0) {
            throw new ApplicationBusinessException("ERROR", "INSUFFICIENT_BALANCE");
        }
    }

}
