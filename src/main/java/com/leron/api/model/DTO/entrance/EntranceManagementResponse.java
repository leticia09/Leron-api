package com.leron.api.model.DTO.entrance;

import com.leron.api.model.DTO.graphic.GraphicResponse;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class EntranceManagementResponse {
    GraphicResponse graphicResponseData;
    GraphicResponse graphicResponseDetails;
    List<EntranceResponse> entranceResponseList;
}
