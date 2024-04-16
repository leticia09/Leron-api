package com.leron.api.model.DTO.graphic;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class Tooltip {
    private String name;
    private List<String> tooltipLabel;
}
