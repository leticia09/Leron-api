package com.leron.api.model.DTO.points;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class TypeScoreDTO {
    private Long id;
    private String description;
}
