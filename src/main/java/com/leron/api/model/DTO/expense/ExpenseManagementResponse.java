package com.leron.api.model.DTO.expense;

import com.leron.api.model.DTO.graphic.GraphicResponse;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class ExpenseManagementResponse {
    GraphicResponse graphicResponseData;
    GraphicResponse graphicResponseDetails;
    List<ExpenseResponse> expenseResponseList;
}
