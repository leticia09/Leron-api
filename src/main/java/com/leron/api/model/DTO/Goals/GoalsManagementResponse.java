package com.leron.api.model.DTO.Goals;

import com.leron.api.model.DTO.graphic.GraphicResponse;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class GoalsManagementResponse {
    GraphicResponse graphicResponseData;
    GraphicResponse graphicResponseDetails;
    List<GoalsResponse> goalsResponseList;
}
