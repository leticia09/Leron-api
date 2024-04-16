package com.leron.api.model.DTO.graphic;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
public class LabelTooltip {
    private String label;
    private ArrayList<Tooltip> tooltipList;

}
