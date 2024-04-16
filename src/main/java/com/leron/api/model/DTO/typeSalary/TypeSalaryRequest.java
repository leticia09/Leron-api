package com.leron.api.model.DTO.typeSalary;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class TypeSalaryRequest {
    private Long id;
    private String description;
    private Long userAuthId;
    private Boolean deleted;
}
