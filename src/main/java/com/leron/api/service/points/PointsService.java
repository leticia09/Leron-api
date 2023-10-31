package com.leron.api.service.points;

import com.leron.api.mapper.PointsMapper;
import com.leron.api.model.DTO.graphic.GraphicResponse;
import com.leron.api.model.DTO.points.PointsRequest;
import com.leron.api.model.DTO.points.PointsResponse;
import com.leron.api.model.DTO.points.TransferRequest;
import com.leron.api.model.DTO.points.TypeScoreDTO;
import com.leron.api.model.entities.*;
import com.leron.api.repository.PointsRepository;
import com.leron.api.repository.TransferRepository;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataRequest;
import com.leron.api.responses.DataResponse;
import com.leron.api.validator.points.PointsValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(rollbackFor = Exception.class)
public class PointsService {

    private final PointsRepository pointsRepository;
    private final TransferRepository transferRepository;

    public PointsService(PointsRepository pointsRepository, TransferRepository transferRepository) {
        this.pointsRepository = pointsRepository;
        this.transferRepository = transferRepository;
    }

    public DataListResponse<PointsResponse> list(Long userAuthId) {
        List<Score> pointsEntityList = pointsRepository.findByUserAuthId(userAuthId);
        return PointsMapper.pointsEntitiesToDataListResponse(pointsEntityList);
    }

    public DataResponse<PointsResponse> create(DataRequest<List<PointsRequest>> request) throws ApplicationBusinessException {
        DataResponse<PointsResponse> response = new DataResponse<>();
        List<Score> pointsEntityList = pointsRepository.findByUserAuthId(request.getData().get(0).getUserAuthId());
        PointsValidator.validator(request.getData(), pointsEntityList);

        List<Score> entity = PointsMapper.createPointsFromRevenueRequest(request.getData());
        pointsRepository.saveAll(entity);
        response.setMessage("Sucesso");
        return response;
    }

    public DataResponse<List<TypeScoreDTO>> getType() {
        DataResponse<List<TypeScoreDTO>> response = new DataResponse<>();

        List<TypeScoreDTO> list = new ArrayList<>();
        TypeScoreDTO typeScoreDTO = new TypeScoreDTO();
        typeScoreDTO.setId(1L);
        typeScoreDTO.setDescription("Milhas");

        list.add(typeScoreDTO);

        TypeScoreDTO typeScoreDTO2 = new TypeScoreDTO();
        typeScoreDTO2.setId(2L);
        typeScoreDTO2.setDescription("Pontos");

        list.add(typeScoreDTO2);

        response.setData(list);
        return response;
    }

    public DataResponse<List<TypeScoreDTO>> getProgramsById(Long userAuthId) {
        DataResponse<List<TypeScoreDTO>> response = new DataResponse<>();
        List<Object[]> list = pointsRepository.findIdAndProgramByUserAuthId(userAuthId);
        response.setData(PointsMapper.mapToObjectList(list));
        return response;
    }

    public DataResponse<TransferRequest> transfer(DataRequest<TransferRequest> request) throws ApplicationBusinessException {
        PointsValidator.validatorTransfer(request.getData());
        Score currentValueOriginProgram = pointsRepository.findScoreById(request.getData().getOriginProgramId(), request.getData().getUserAuthId());
        Score currentValueDestinyProgram = pointsRepository.findScoreById(request.getData().getDestinyProgramId(), request.getData().getUserAuthId());

        return doTransfer(request.getData(), currentValueOriginProgram, currentValueDestinyProgram);
    }

    private DataResponse<TransferRequest> doTransfer(TransferRequest request, Score currentValueOriginProgram, Score currentValueDestinyProgram) throws ApplicationBusinessException {
        DataResponse<TransferRequest> response = new DataResponse<>();

        BigDecimal saveOriginValue = currentValueOriginProgram.getValue().subtract(request.getQuantity());

        currentValueOriginProgram.setValue(saveOriginValue);

        BigDecimal valueMulti = request.getDestinyValue().multiply(request.getQuantity());
        BigDecimal quantity = currentValueDestinyProgram.getValue().add(valueMulti);

        if (Objects.nonNull(request.getBonus())) {
            BigDecimal divides = request.getBonus().divide(BigDecimal.valueOf(100));
            BigDecimal result = valueMulti.multiply(divides);
            quantity = quantity.add(result);
        }

        currentValueDestinyProgram.setValue(quantity);

        PointsValidator.validatorValueTransfer(currentValueOriginProgram, currentValueDestinyProgram);

        pointsRepository.save(currentValueOriginProgram);
        pointsRepository.save(currentValueDestinyProgram);
        transferRepository.save(PointsMapper.TransferRequestToEntity(request));

        response.setMessage("Sucesso");


        return response;
    }

    public DataResponse<GraphicResponse> getProgramsData(Long authId) {
        DataResponse<GraphicResponse> response = new DataResponse<>();
        List<Score> programData = pointsRepository.findByUserAuthId(authId);

        BigDecimal totalMiles = new BigDecimal(0);
        BigDecimal totalPoints = new BigDecimal(0);
        BigDecimal totalProgramActive = new BigDecimal(0);
        BigDecimal totalProgramInactive = new BigDecimal(0);

        GraphicResponse graphicResponse = new GraphicResponse();

        ArrayList<String> labels = new ArrayList<>();
        ArrayList<BigDecimal> data = new ArrayList<>();

        for (Score result : programData) {
            String program = result.getProgram();
            BigDecimal value = result.getValue();
            labels.add(program);
            data.add(value);

            if(Objects.equals(result.getTypeOfScore(), "Milhas")) {
                totalPoints = result.getValue().add(totalPoints);
            }

            if(Objects.equals(result.getTypeOfScore(), "Pontos")) {
                totalMiles = result.getValue().add(totalMiles);
            }

            if(result.getStatus().equals("ACTIVE")) {
                totalProgramActive = totalProgramActive.add(BigDecimal.ONE);
            }

            if(result.getStatus().equals("INACTIVE")) {
                totalProgramInactive = totalProgramInactive.add(BigDecimal.ONE);
            }
        }

        graphicResponse.setLabels(labels);
        graphicResponse.setData(data);

        graphicResponse.setTotalPoints(totalPoints);
        graphicResponse.setTotalMiles(totalMiles);
        graphicResponse.setTotalProgramInactive(totalProgramInactive);
        graphicResponse.setTotalProgramActive(totalProgramActive);

        response.setData(graphicResponse);

        return response;
    }

}
