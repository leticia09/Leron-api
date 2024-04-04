package com.leron.api.service.forecast;

import com.leron.api.mapper.forecast.ForecastMapper;
import com.leron.api.model.DTO.entrance.EntranceResponse;
import com.leron.api.model.DTO.expense.ExpenseResponse;
import com.leron.api.model.DTO.forecast.ForecastManagementResponse;
import com.leron.api.model.DTO.forecast.ForecastPrevResponse;
import com.leron.api.model.DTO.forecast.ForecastRequest;
import com.leron.api.model.DTO.forecast.ForecastResponse;
import com.leron.api.model.DTO.graphic.DataSet;
import com.leron.api.model.DTO.graphic.GraphicResponse;
import com.leron.api.model.entities.Forecast;
import com.leron.api.model.entities.ForecastDate;
import com.leron.api.repository.BankMovementRepository;
import com.leron.api.repository.ForecastDateRepository;
import com.leron.api.repository.ForecastRepository;
import com.leron.api.repository.MacroGroupRepository;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataResponse;
import com.leron.api.service.entrance.EntranceService;
import com.leron.api.service.expense.ExpenseService;
import com.leron.api.validator.forecast.ValidatorForecast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import static com.leron.api.utils.FormatDate.getMonth;
import static com.leron.api.utils.FormatDate.populateMonths;

@Service
public class ForecastService {

    @Autowired
    ExpenseService expenseService;

    @Autowired
    EntranceService entranceService;

    final ForecastRepository forecastRepository;
    final BankMovementRepository bankMovementRepository;
    final MacroGroupRepository macroGroupRepository;
    final ForecastDateRepository forecastDateRepository;

    public ForecastService(ForecastRepository forecastRepository, BankMovementRepository bankMovementRepository, MacroGroupRepository macroGroupRepository, ForecastDateRepository forecastDateRepository) {
        this.forecastRepository = forecastRepository;
        this.bankMovementRepository = bankMovementRepository;
        this.macroGroupRepository = macroGroupRepository;
        this.forecastDateRepository = forecastDateRepository;
    }

    public DataResponse<ForecastResponse> createForecast(List<ForecastRequest> forecastRequest) throws ApplicationBusinessException {
        DataResponse<ForecastResponse> response = new DataResponse<>();

        List<Forecast> forecasts = forecastRepository.findAllByUserAuthIdAndDeletedFalse(forecastRequest.get(0).getUserAuthId());

        ValidatorForecast.validateCreation(forecastRequest, forecasts);
        List<Forecast> forecastToSave = ForecastMapper.requestToEntity(forecastRequest);

        forecastToSave.forEach(forecast -> {
            Forecast forecastSaved = forecastRepository.save(forecast);
            forecast.getForecastDates().forEach(forecastDate -> {
                forecastDate.setForecast(forecastSaved);
                forecastDateRepository.save(forecastDate);
            });
        });

        response.setSeverity("success");
        response.setMessage("success");

        return response;
    }

    public DataResponse<ForecastManagementResponse> getManagementScreen(Long userAuthId, int month, Long year, List<Long> owners) {
        DataResponse<ForecastManagementResponse> response = new DataResponse<>();
        ForecastManagementResponse forecastManagementResponse = new ForecastManagementResponse();

        List<Forecast> forecasts = listForecast(userAuthId, month, year, owners);
        List<ForecastDate> forecastsAllYear = listForecastAllYear(userAuthId, owners, year);
        DataListResponse<ExpenseResponse> expenses = expenseService.list(userAuthId, month, year.intValue(), owners);

        forecastManagementResponse.setGraphicResponse(graphic(userAuthId, month, year, owners));
        forecastManagementResponse.setForecastPrevResponseList(forecastPrev(forecasts, expenses));
        forecastManagementResponse.setForecastResponseList(forecastList(forecastsAllYear));

        response.setData(forecastManagementResponse);
        return response;
    }

    public List<Forecast> listForecast(Long userAuthId, int month, Long year, List<Long> owners) {
        return forecastRepository.findAllByUserAuthIdAndMonthAndYearAndOwnersId(userAuthId, getMonth(month), year, owners);
    }

    public List<ForecastDate> listForecastAllYear(Long userAuthId, List<Long> owners, Long year) {
        List<Long> forecastId = forecastRepository.findAllByUserAuthIdAndOwnersId(userAuthId, owners);
        return forecastDateRepository.findAllByUserAuthIdAndMonthAndYearAndOwnersId(forecastId, year);
    }

    public GraphicResponse graphic(Long userAuthId, int month, Long year, List<Long> owners) {
        GraphicResponse response = new GraphicResponse();

        ArrayList<String> labels = populateMonths();
        ArrayList<DataSet> dataSets = new ArrayList<>();

        dataSets.add(populateEntrances(userAuthId, year.intValue(), owners));
        dataSets.add(populateExpenses(userAuthId, year.intValue(), owners));
        dataSets.add(populateLeft(dataSets.get(0), dataSets.get(1)));

        DataSet percent = populateLeftPercent(dataSets.get(0), dataSets.get(1));

        response.setDataSet(dataSets);
        response.setLabels(labels);
        response.setTotal1(getTotal(dataSets.get(0).getData(), month));
        response.setTotal2(getTotal(dataSets.get(1).getData(), month));
        response.setTotal3(getTotal(dataSets.get(2).getData(), month));
        response.setTotal4(getTotal(percent.getData(), month));
        response.setTotal5(getTotalLeft(dataSets.get(2).getData()));

        return response;
    }

    public List<ForecastPrevResponse> forecastPrev(List<Forecast> forecasts, DataListResponse<ExpenseResponse> expenses) {
        return ForecastMapper.entityToForecastPrevResponse(forecasts, expenses.getData());
    }

    public List<ForecastResponse> forecastList(List<ForecastDate> forecastDates) {
        return ForecastMapper.entityToForecastResponse(forecastDates);
    }

    public DataSet populateEntrances(Long userAuthId, int year, List<Long> owners) {
        DataSet dataSet = new DataSet();
        ArrayList<BigDecimal> data = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            DataListResponse<EntranceResponse> entrances = entranceService.list(userAuthId, i, year, owners);
            List<EntranceResponse> entranceFilteredReceived = entrances.getData().stream().filter(entrance ->
                    entrance.getStatus().equalsIgnoreCase("confirmado")
            ).collect(Collectors.toList());

            List<EntranceResponse> entranceFilteredForecast = entrances.getData().stream().filter(entrance ->
                    !entrance.getStatus().equalsIgnoreCase("confirmado")
            ).collect(Collectors.toList());

            BigDecimal totalReceived = entranceFilteredReceived.stream()
                    .map(EntranceResponse::getValueReceived)
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);

            BigDecimal totalForecast = entranceFilteredForecast.stream()
                    .map(EntranceResponse::getSalary)
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);

            data.add(totalReceived.add(totalForecast));
        }

        dataSet.setLabel("Receita");
        dataSet.setBackgroundColor("rgba(1,184,170,0.4)");
        dataSet.setBorderColor("#01b8aa");
        dataSet.setFill(true);
        dataSet.setData(data);
        return dataSet;
    }

    public DataSet populateExpenses(Long userAuthId, int year, List<Long> owners) {
        DataSet dataSet = new DataSet();
        ArrayList<BigDecimal> data = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        int currentMonth = calendar.get(Calendar.MONTH) + 1;

        for (int i = 1; i <= 12; i++) {
            List<Forecast> forecasts = listForecast(userAuthId, i, (long) year, owners);
            DataListResponse<ExpenseResponse> expenses = expenseService.list(userAuthId, i, year, owners);
            List<ForecastPrevResponse> forecastPrevList = forecastPrev(forecasts, expenses);
            BigDecimal totalForecast = getTotalForecast(i, currentMonth, forecastPrevList);

            List<ExpenseResponse> expenseFilteredPaid = expenses.getData().stream().filter(expense ->
                    expense.getStatus().equalsIgnoreCase("confirmado")
            ).collect(Collectors.toList());

            List<ExpenseResponse> expenseFilteredNotPaid = expenses.getData().stream().filter(expense ->
                    !expense.getStatus().equalsIgnoreCase("confirmado")
            ).collect(Collectors.toList());


            List<ExpenseResponse> expenseFilteredSplitNotPaid = expenseFilteredNotPaid.stream().filter(
                    ExpenseResponse::getHasSplitExpense
            ).collect(Collectors.toList());

            List<ExpenseResponse> expenseFilteredNotSplitNotPaid = expenseFilteredNotPaid.stream().filter(expense ->
                    !expense.getHasSplitExpense()
            ).collect(Collectors.toList());

            BigDecimal totalPaid = expenseFilteredPaid.stream()
                    .map(ExpenseResponse::getValuePaid)
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);


            BigDecimal totalSplit = expenseFilteredSplitNotPaid.stream()
                    .map(ExpenseResponse::getPartValue)
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);


            BigDecimal totalNotSplitNotPaid = expenseFilteredNotSplitNotPaid.stream()
                    .map(ExpenseResponse::getValue)
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);

            data.add(totalPaid.add(totalSplit.add(totalNotSplitNotPaid).add(totalForecast)));
        }

        dataSet.setLabel("Despesa");
        dataSet.setBackgroundColor("rgba(255,0,0,0.4)");
        dataSet.setBorderColor("red");
        dataSet.setFill(true);
        dataSet.setData(data);
        return dataSet;
    }

    private static BigDecimal getTotalForecast(int i, int currentMonth, List<ForecastPrevResponse> forecastPrevList) {
        BigDecimal totalForecast = new BigDecimal(BigInteger.ZERO);

        if (i >= currentMonth) {
            for (ForecastPrevResponse forecast : forecastPrevList) {
                if (forecast.getDifference().compareTo(BigDecimal.ZERO) < 0) {
                    BigDecimal difference = forecast.getDifference().negate();
                    totalForecast = totalForecast.add(difference);
                } else {
                    totalForecast = totalForecast.add(forecast.getDifference());
                }
            }
        }
        return totalForecast;
    }


    private static DataSet populateLeft(DataSet receive, DataSet expense) {
        DataSet dataSet = new DataSet();
        ArrayList<BigDecimal> data = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            BigDecimal receiveValue = receive.getData().get(i);
            BigDecimal expenseValue = expense.getData().get(i);
            BigDecimal difference = receiveValue.subtract(expenseValue);
            data.add(difference);
        }

        dataSet.setLabel("Sobra");
        dataSet.setBackgroundColor("rgba(1,149,245,0.4)");
        dataSet.setBorderColor("#0195f5");
        dataSet.setFill(true);
        dataSet.setData(data);
        return dataSet;
    }

    private static DataSet populateLeftPercent(DataSet receive, DataSet expense) {
        DataSet dataSet = new DataSet();
        ArrayList<BigDecimal> data = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            BigDecimal monthDiff = receive.getData().get(i).subtract(expense.getData().get(i));

            if (!receive.getData().get(i).equals(BigDecimal.ZERO)) {
                BigDecimal percent = monthDiff.divide(receive.getData().get(i), 2, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
                data.add(percent);
            } else {
                data.add(BigDecimal.ZERO);
            }
        }

        dataSet.setLabel("Sobra");
        dataSet.setBackgroundColor("#0195f5");
        dataSet.setBorderColor("#0195f5");
        dataSet.setFill(true);
        dataSet.setData(data);
        return dataSet;
    }

    private static BigDecimal getTotal(ArrayList<BigDecimal> dataList, int index) {
        BigDecimal receiveTotal = new BigDecimal(BigInteger.ZERO);
        if (index == 1) {
            receiveTotal = dataList.get(0);
        } else if (index == 2) {
            receiveTotal = dataList.get(1);
        } else if (index == 3) {
            receiveTotal = dataList.get(2);
        } else if (index == 4) {
            receiveTotal = dataList.get(3);
        } else if (index == 5) {
            receiveTotal = dataList.get(4);
        } else if (index == 6) {
            receiveTotal = dataList.get(5);
        } else if (index == 7) {
            receiveTotal = dataList.get(6);
        } else if (index == 8) {
            receiveTotal = dataList.get(7);
        } else if (index == 9) {
            receiveTotal = dataList.get(8);
        } else if (index == 10) {
            receiveTotal = dataList.get(9);
        } else if (index == 11) {
            receiveTotal = dataList.get(10);
        } else if (index == 12) {
            receiveTotal = dataList.get(11);
        }
        return receiveTotal;
    }

    private static BigDecimal getTotalLeft(ArrayList<BigDecimal> dataList) {
        BigDecimal receiveTotal = new BigDecimal(BigInteger.ZERO);
        for (BigDecimal bigDecimal : dataList) {
            receiveTotal = receiveTotal.add(bigDecimal);
        }
        return receiveTotal;
    }

}
