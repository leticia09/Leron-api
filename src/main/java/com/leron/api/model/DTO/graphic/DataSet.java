package com.leron.api.model.DTO.graphic;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;

@Component
@Data
public class DataSet {
    private ArrayList<BigDecimal> data;
    private String label;
    private String backgroundColor;
    private String borderColor;
    private Boolean fill;
}


