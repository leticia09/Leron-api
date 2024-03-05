package com.leron.api.service.typeSalary;

import com.leron.api.mapper.TypeSalary.TypeSalaryMapper;
import com.leron.api.model.DTO.typeSalary.TypeSalaryRequest;
import com.leron.api.model.entities.TypeSalary;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataRequest;
import com.leron.api.responses.DataResponse;
import org.springframework.stereotype.Service;
import com.leron.api.repository.TypeSalaryRepository;
import java.util.List;

@Service
public class TypeSalaryService {
    private final TypeSalaryRepository typeSalaryRepository;

    public TypeSalaryService(TypeSalaryRepository typeSalaryRepository) {
        this.typeSalaryRepository = typeSalaryRepository;
    }

    public List<TypeSalary> list(Long userAuthId){
        return typeSalaryRepository.findAllByUserAuthIdOrderByDescription(userAuthId);
    }

    public DataResponse<TypeSalary> edit(DataRequest<List<TypeSalaryRequest>> request) throws ApplicationBusinessException {
        DataResponse<TypeSalary> response = new DataResponse<>();
        List<TypeSalary> typeSalaryList = typeSalaryRepository.findAllByUserAuthIdOrderByDescription(request.getData().get(0).getUserAuthId());
        typeSalaryRepository.deleteAll(typeSalaryList);
        List<TypeSalary> typeSalaries = TypeSalaryMapper.requestToEntity(request.getData());
        typeSalaryRepository.saveAll(typeSalaries);

        response.setMessage("success");
        response.setSeverity("success");
        return response;
    }
}
