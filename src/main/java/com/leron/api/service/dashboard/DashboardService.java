package com.leron.api.service.dashboard;

import com.leron.api.model.DTO.dashboard.DashboardManagementResponse;
import com.leron.api.model.DTO.graphic.DataSet;
import com.leron.api.model.DTO.graphic.GraphicResponse;
import com.leron.api.model.entities.Member;
import com.leron.api.repository.MemberRepository;
import com.leron.api.responses.DataResponse;
import com.leron.api.service.forecast.ForecastService;
import com.leron.api.service.movementBank.MovementBankService;
import com.leron.api.service.points.PointsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.leron.api.utils.FormatDate.populateMonths;

@Service
public class DashboardService {

    @Autowired
    ForecastService forecastService;

    @Autowired
    MovementBankService movementBankService;

    @Autowired
    PointsService pointsService;

    final MemberRepository memberRepository;

    public DashboardService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public DataResponse<DashboardManagementResponse> getManagementData(Long authId) {
        DataResponse<DashboardManagementResponse> response = new DataResponse<>();
        DashboardManagementResponse managementResponse = new DashboardManagementResponse();
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear();
        int month = currentDate.getMonthValue();

        List<Member> members = memberRepository.findAllByUserAuthIdAndDeletedFalseOrderByNameAsc(authId);
        List<Long> owners = members.stream().map(Member::getId).collect(Collectors.toList());

        GraphicResponse graphicResponseEntranceExpenseMonthTotal = graphicEntranceXExpenseData(authId, year, owners, month, month);
        GraphicResponse graphicResponseEntranceExpense = graphicEntranceXExpenseData(authId, year, owners, month, 12);
        GraphicResponse graphicPatrimonyResponse = graphicPatrimony(authId);
        GraphicResponse graphicPointsResponse = graphicPointsData(authId);

        BigDecimal totalEntrance = graphicResponseEntranceExpense.getDataSet().get(0).getData()
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = graphicResponseEntranceExpenseMonthTotal.getDataSet().get(1).getData()
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPatrimony = graphicPatrimonyResponse.getDataSet().get(0).getData()
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPoints = graphicPointsResponse.getDataSet().get(0).getData()
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        managementResponse.setGraphicEntranceXExpenseData(graphicResponseEntranceExpense);
        managementResponse.setGraphicPointsData(graphicPointsResponse);
        managementResponse.setGraphicPatrimony(graphicPatrimonyResponse);
        managementResponse.setGraphicGoal(graphicGoal(authId, year, owners));

        managementResponse.setTotalEntrance(totalEntrance);
        managementResponse.setTotalExpense(totalExpense);
        managementResponse.setTotalLeft(totalEntrance.subtract(totalExpense));
        managementResponse.setTotalPoints(totalPoints);
        managementResponse.setTotalPatrimony(totalPatrimony);

        response.setData(managementResponse);
        return response;
    }

    private GraphicResponse graphicEntranceXExpenseData(Long authId, int year, List<Long> owners, int maxMonthEntrance, int maxMonthExpense) {
        GraphicResponse response = new GraphicResponse();

        ArrayList<String> labels = populateMonths();
        ArrayList<DataSet> dataSets = new ArrayList<>();

        dataSets.add(forecastService.populateEntrances(authId, year, owners, maxMonthEntrance));
        dataSets.add(forecastService.populateExpenses(authId, year, owners, true, maxMonthExpense));

        response.setDataSet(dataSets);
        response.setLabels(labels);
        return response;
    }

    private GraphicResponse graphicPointsData(Long authId) {
        return  pointsService.getGraphicWithoutMember(authId);
    }

    private GraphicResponse graphicPatrimony(Long authId) {
       return  movementBankService.getGraphicWithoutMember(authId);
    }

    private GraphicResponse graphicGoal(Long authId, int year, List<Long> owners) {
        GraphicResponse response = new GraphicResponse();

        return response;
    }
}
