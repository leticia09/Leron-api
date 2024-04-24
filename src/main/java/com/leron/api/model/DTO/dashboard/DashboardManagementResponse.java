package com.leron.api.model.DTO.dashboard;

import com.leron.api.model.DTO.graphic.GraphicResponse;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Data
public class DashboardManagementResponse {
    GraphicResponse graphicEntranceXExpenseData;
    GraphicResponse graphicPointsData;
    GraphicResponse graphicPatrimony;
    GraphicResponse graphicGoal;

    BigDecimal totalEntrance;
    BigDecimal totalExpense;
    BigDecimal totalLeft;
    BigDecimal totalPoints;
    BigDecimal totalPatrimony;
}
