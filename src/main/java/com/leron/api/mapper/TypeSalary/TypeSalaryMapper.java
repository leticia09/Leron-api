package com.leron.api.mapper.TypeSalary;

import com.leron.api.model.DTO.typeSalary.TypeSalaryRequest;
import com.leron.api.model.entities.TypeSalary;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class TypeSalaryMapper {

    public static List<TypeSalary> requestToEntity(List<TypeSalaryRequest> requests) {
        List<TypeSalary> list = new ArrayList<>();
        requests.forEach(request -> {
            TypeSalary typeSalary = new TypeSalary();
            if (Objects.nonNull(request.getId())) {
                typeSalary.setId(request.getId());
            }
            typeSalary.setDescription(request.getDescription());
            typeSalary.setDeleted(request.getDeleted());
            typeSalary.setUserAuthId(request.getUserAuthId());

            list.add(typeSalary);
        });

        return list;
    }
}
