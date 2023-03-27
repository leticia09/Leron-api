package com.leron.api.service.salary;

import com.leron.api.mapper.salary.SalaryMapper;
import com.leron.api.model.DTO.salary.SalaryRequest;
import com.leron.api.model.DTO.salary.SalaryResponse;
import com.leron.api.model.entities.SalaryEntity;
import com.leron.api.model.entities.UserEntity;
import com.leron.api.repository.SalaryRepository;
import com.leron.api.repository.UserRepository;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataRequest;
import com.leron.api.responses.DataResponse;
import com.leron.api.validator.salary.SalaryValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalaryService {

    private final SalaryRepository salaryRepository;
    private final UserRepository userRepository;

    public SalaryService(SalaryRepository salaryRepository, UserRepository userRepository) {
        this.salaryRepository = salaryRepository;
        this.userRepository = userRepository;
    }

    public DataListResponse<SalaryResponse> list(Long userAuthId){
        List<UserEntity> userEntityList = userRepository.findAllByAuthUserId(userAuthId);
        return SalaryMapper.salaryEntitiesToDataListResponse(salaryRepository.findAllByAuthUserId(userAuthId), userEntityList);
    }

    public DataResponse<SalaryResponse> create(DataRequest<SalaryRequest> userRequest) throws ApplicationBusinessException {
        DataResponse<SalaryResponse> response = new DataResponse<>();
        SalaryValidator.validatorSalary(userRequest);

        SalaryEntity salary = SalaryMapper.createSalaryFromSalaryRequest(userRequest.getData());
        salaryRepository.save(salary);
        SalaryResponse userResponse = SalaryMapper.createSalaryResponse(salary);
        response.setData(userResponse);
        response.setMessage("Sucesso");
        return response;
    }

}
