package com.leron.api.service.points;

import com.leron.api.mapper.score.PointsMapper;
import com.leron.api.model.DTO.graphic.DataSet;
import com.leron.api.model.DTO.graphic.GraphicResponse;
import com.leron.api.model.DTO.points.*;
import com.leron.api.model.entities.*;
import com.leron.api.repository.MemberRepository;
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
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class PointsService {

    private final PointsRepository pointsRepository;
    private final TransferRepository transferRepository;

    private final MemberRepository memberRepository;

    public PointsService(PointsRepository pointsRepository, TransferRepository transferRepository, MemberRepository memberRepository) {
        this.pointsRepository = pointsRepository;
        this.transferRepository = transferRepository;
        this.memberRepository = memberRepository;
    }

    public DataListResponse<PointsResponse> list(Long userAuthId) {
        List<Score> pointsEntityList = pointsRepository.findByUserAuthId(userAuthId);
        List<Member> entities = memberRepository.findAll();
        return PointsMapper.pointsEntitiesToDataListResponse(pointsEntityList, entities);
    }

    public DataResponse<PointsResponse> create(DataRequest<List<PointsRequest>> request) throws ApplicationBusinessException {
        DataResponse<PointsResponse> response = new DataResponse<>();
        List<Score> pointsEntityList = pointsRepository.findByUserAuthId(request.getData().get(0).getUserAuthId());
        PointsValidator.validator(request.getData(), pointsEntityList);

        List<Score> entity = PointsMapper.createPointsFromRevenueRequest(request.getData());
        pointsRepository.saveAll(entity);
        response.setMessage("success");
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

    public DataResponse<List<TypeScoreDTO>> getStatus() {
        DataResponse<List<TypeScoreDTO>> response = new DataResponse<>();

        List<TypeScoreDTO> list = new ArrayList<>();
        TypeScoreDTO typeScoreDTO = new TypeScoreDTO();
        typeScoreDTO.setId(1L);
        typeScoreDTO.setDescription("Ativo");

        list.add(typeScoreDTO);

        TypeScoreDTO typeScoreDTO2 = new TypeScoreDTO();
        typeScoreDTO2.setId(2L);
        typeScoreDTO2.setDescription("Inativo");

        list.add(typeScoreDTO2);

        response.setData(list);
        return response;
    }

    public DataResponse<List<TypeScoreDTO>> getProgramsByAuth(Long userAuthId) {
        DataResponse<List<TypeScoreDTO>> response = new DataResponse<>();
        List<Object[]> list = pointsRepository.findIdAndProgramByUserAuthId(userAuthId);
        response.setData(PointsMapper.mapToObjectList(list));
        return response;
    }

    public DataResponse<List<TypeScoreDTO>> getProgramsById(Long id) {
        DataResponse<List<TypeScoreDTO>> response = new DataResponse<>();
        List<Score> list = pointsRepository.findAllByOwnerId(id);
        response.setData(PointsMapper.mapScoreToObjectList(list));
        return response;
    }

    public DataResponse<TransferRequest> transfer(DataRequest<TransferRequest> request) throws ApplicationBusinessException {
        PointsValidator.validatorTransfer(request.getData());
        Score currentValueOriginProgram = pointsRepository.findScoreById(
                request.getData().getOriginProgramId(),
                request.getData().getUserAuthId(),
                request.getData().getOwnerIdOrigin()
        );
        Score currentValueDestinyProgram = pointsRepository.findScoreById(
                request.getData().getDestinyProgramId(),
                request.getData().getUserAuthId(),
                request.getData().getOwnerIdDestiny()
        );

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

        response.setMessage("success");


        return response;
    }

    public DataResponse<GraphicResponse> getProgramsData(Long authId) {
        DataResponse<GraphicResponse> response = new DataResponse<>();
        List<Score> programData = pointsRepository.findByUserAuthId(authId);
        List<Member> members = memberRepository.findAllByUserAuthIdAndDeletedFalseOrderByNameAsc(authId);

        BigDecimal totalMiles = BigDecimal.ZERO;
        BigDecimal totalPoints = BigDecimal.ZERO;
        BigDecimal totalProgramActive = BigDecimal.ZERO;
        BigDecimal totalProgramInactive = BigDecimal.ZERO;

        GraphicResponse graphicResponse = new GraphicResponse();

        ArrayList<DataSet> dataSets = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        for (Member member : members) {
            DataSet dataSet = new DataSet();
            ArrayList<BigDecimal> data = new ArrayList<>(Collections.nCopies(labels.size(), BigDecimal.ZERO));

            for (Score score : programData) {
                if (member.getId().equals(score.getOwnerId())) {
                    int labelIndex = labels.indexOf(score.getProgram());
                    if (labelIndex == -1) {
                        labels.add(score.getProgram());
                        data.add(score.getValue());
                    } else {
                        data.set(labelIndex, data.get(labelIndex).add(score.getValue()));
                    }

                    if (score.getTypeOfScore().equals("Pontos")) {
                        totalPoints = totalPoints.add(score.getValue());
                    } else if (score.getTypeOfScore().equals("Milhas")) {
                        totalMiles = totalMiles.add(score.getValue());
                    }

                    if (score.getStatus().equals("ACTIVE")) {
                        totalProgramActive = totalProgramActive.add(BigDecimal.ONE);
                    } else if (score.getStatus().equals("INACTIVE")) {
                        totalProgramInactive = totalProgramInactive.add(BigDecimal.ONE);
                    }
                }
            }

            dataSet.setLabel(member.getName());
            dataSet.setBackgroundColor(member.getColor());
            dataSet.setBorderColor(member.getColor());
            dataSet.setData(data);
            dataSets.add(dataSet);
        }

        graphicResponse.setDataSet(dataSets);
        graphicResponse.setLabels(labels);
        graphicResponse.setTotalPoints(totalPoints);
        graphicResponse.setTotalMiles(totalMiles);
        graphicResponse.setTotalProgramInactive(totalProgramInactive);
        graphicResponse.setTotalProgramActive(totalProgramActive);

        response.setData(graphicResponse);

        return response;
    }


    public DataResponse<StatusRequest> updateStatus(DataRequest<StatusRequest> request) throws ApplicationBusinessException {
        DataResponse<StatusRequest> response = new DataResponse<>();

        PointsValidator.validateStatus(request.getData());

        Score program = pointsRepository.findScoreByIdWithoutOwner(
                request.getData().getProgramId(),
                request.getData().getUserAuthId()
        );

        if (request.getData().getStatus().equals("Ativo")) {
            program.setStatus("ACTIVE");
        }

        if (request.getData().getStatus().equals("Inativo")) {
            program.setValue(BigDecimal.ZERO);
            program.setPointsExpirationDate(null);
            program.setStatus("INACTIVE");
        }

        pointsRepository.save(program);

        response.setMessage("success");

        return response;
    }

    public DataResponse<UseRequest> usePoints(DataRequest<UseRequest> request) throws ApplicationBusinessException {
        DataResponse<UseRequest> response = new DataResponse<>();

        Score program = pointsRepository.findScoreById(
                request.getData().getProgramId(),
                request.getData().getUserAuthId(),
                request.getData().getOwnerId()
        );

        BigDecimal valueActual = program.getValue().subtract(request.getData().getValue());

        PointsValidator.validateValue(valueActual);


        program.setValue(valueActual);
        program.setChangedIn(new Date());

        pointsRepository.save(program);

        response.setMessage("success");

        return response;
    }

    public DataResponse<PointsResponse> delete(Long id) throws ApplicationBusinessException {
        DataResponse<PointsResponse> response = new DataResponse<>();
        Optional<Score> current = pointsRepository.findById(id);
        if (current.isPresent()) {
            current.get().setDeleted(true);
            pointsRepository.save(current.get());
        }

        response.setMessage("success");
        return response;
    }

}
