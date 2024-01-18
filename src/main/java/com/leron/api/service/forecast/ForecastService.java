package com.leron.api.service.forecast;

import com.leron.api.mapper.forecast.ForecastMapper;
import com.leron.api.model.DTO.forecast.ForecastPrevResponse;
import com.leron.api.model.DTO.forecast.ForecastRequest;
import com.leron.api.model.DTO.forecast.ForecastResponse;
import com.leron.api.model.DTO.graphic.DataSet;
import com.leron.api.model.DTO.graphic.GraphicResponse;
import com.leron.api.model.entities.*;
import com.leron.api.repository.*;
import com.leron.api.responses.ApplicationBusinessException;
import com.leron.api.responses.DataListResponse;
import com.leron.api.responses.DataResponse;
import com.leron.api.utils.GetStatusPayment;
import com.leron.api.validator.forecast.ValidatorForecast;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ForecastService {
    final ForecastRepository forecastRepository;
    final EntranceRepository entranceRepository;
    final ExpenseRepository expenseRepository;
    final BankMovementRepository bankMovementRepository;
    final SpecificGroupRepository specificGroupRepository;
    final MacroGroupRepository macroGroupRepository;

    public ForecastService(ForecastRepository forecastRepository, EntranceRepository entranceRepository, ExpenseRepository expenseRepository, BankMovementRepository bankMovementRepository, SpecificGroupRepository specificGroupRepository, MacroGroupRepository macroGroupRepository) {
        this.forecastRepository = forecastRepository;
        this.entranceRepository = entranceRepository;
        this.expenseRepository = expenseRepository;
        this.bankMovementRepository = bankMovementRepository;
        this.specificGroupRepository = specificGroupRepository;
        this.macroGroupRepository = macroGroupRepository;
    }

    public DataResponse<ForecastResponse> createForecast(List<ForecastRequest> forecastRequest) throws ApplicationBusinessException {
        DataResponse<ForecastResponse> response = new DataResponse<>();

        List<Forecast> forecasts = forecastRepository.findAllByUserAuthIdAndDeletedFalse(forecastRequest.get(0).getUserAuthId());

        ValidatorForecast.validateCreation(forecastRequest, forecasts);

        forecastRepository.saveAll(ForecastMapper.requestToEntity(forecastRequest));

        response.setSeverity("success");
        response.setMessage("success");

        return response;
    }

    public DataListResponse<ForecastResponse> list(Long userAuthId, int month, int year, List<Long> owners) {
        List<Forecast> forecastList = forecastRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);
        return ForecastMapper.entityToResponse(month, year, forecastList, owners);
    }

    public DataListResponse<ForecastPrevResponse> listPrev(Long userAuthId, int month, int year, List<Long> owners) {
        List<Forecast> forecastList = forecastRepository.findAllByUserAuthIdAndDeletedFalseAndOwnersId(userAuthId, owners);
        List<Expense> expenses = expenseRepository.findAllByUserAuthIdAndOwnersId(userAuthId, owners);
        String period = (month < 10) ? "0" + month + "/" + year : month + "/" + year;
        List<BankMovement> bankMovementList = bankMovementRepository.findAllByUserAuthIdAndDeletedFalseAndReferencePeriodAndOwnersId(userAuthId, period, owners);
        List<MacroGroup> macroGroupList = macroGroupRepository.findAllByUserAuthIdAndDeletedFalseOrderByNameAsc(userAuthId);
        List<Expense> expensesFiltered = new ArrayList<>();
        for (Expense expense : expenses) {
            String status = GetStatusPayment.getStatus(expense, bankMovementList, month, year);
            if (!status.equalsIgnoreCase("Não Iniciada") && !status.isEmpty()) {
                LocalDate initialDate;
                if (Objects.nonNull(expense.getInitialDate())) {
                    initialDate = expense.getInitialDate().toLocalDateTime().toLocalDate();
                } else {
                    initialDate = expense.getDateBuy().toLocalDateTime().toLocalDate();
                }
                int initialMonth = initialDate.getMonthValue();
                int initialYear = initialDate.getYear();
                if (status.equalsIgnoreCase("Confirmado")) {
                    if (initialMonth == month && initialYear == year) {
                        expense.setStatus(status);
                        expensesFiltered.add(expense);
                    }
                } else {
                    expense.setStatus(status);
                    expensesFiltered.add(expense);
                }
            }
        }


        return ForecastMapper.entityToResponsePrev(month, year, forecastList, expensesFiltered, macroGroupList);
    }

    public DataListResponse<ForecastResponse> list(Long userAuthId) {
        List<Forecast> forecastList = forecastRepository.findAllByUserAuthIdAndDeletedFalse(userAuthId);
        return ForecastMapper.entityToResponse(forecastList);
    }

    public DataResponse<GraphicResponse> getData(Long authId, int month, int year, List<Long> owners) {
        DataResponse<GraphicResponse> response = new DataResponse<>();
        List<Entrance> entrances = entranceRepository.findAllByUserAuthIdAndOwnersId(authId, owners);
        List<Expense> expenses = expenseRepository.findAllByUserAuthIdAndOwnersId(authId, owners);
        List<Forecast> forecasts = forecastRepository.findAllByUserAuthIdAndDeletedFalseAndOwnersId(authId, owners);
        List<BankMovement> bankMovements = bankMovementRepository.findAllByUserAuthIdAndDeletedFalseAndOwnersId(authId, owners);
        List<SpecificGroup> specificGroups = specificGroupRepository.findAllByUserAuthIdAndDeletedFalse(authId);
        GraphicResponse graphicResponse = new GraphicResponse();

        ArrayList<String> labels = populateMonths();
        ArrayList<DataSet> dataSets = new ArrayList<>();

        dataSets.add(populateEntrances(entrances, month, year, owners));
        dataSets.add(populateExpenses(expenses, bankMovements, month, year, owners, forecasts, specificGroups));
        dataSets.add(populateLeft(dataSets.get(0), dataSets.get(1)));

        DataSet percent = populateLeftPercent(dataSets.get(0), dataSets.get(1));
        graphicResponse.setDataSet(dataSets);
        graphicResponse.setLabels(labels);
        graphicResponse.setTotal1(getTotal(dataSets.get(0).getData(), month));
        graphicResponse.setTotal2(getTotal(dataSets.get(1).getData(), month));
        graphicResponse.setTotal3(getTotal(dataSets.get(2).getData(), month));
        graphicResponse.setTotal4(getTotal(percent.getData(), month));
        graphicResponse.setTotal5(getTotalLeft(dataSets.get(2).getData()));

        response.setData(graphicResponse);

        return response;
    }

    private static ArrayList<String> populateMonths() {
        ArrayList<String> months = new ArrayList<>();
        months.add("Janeiro");
        months.add("Fevereiro");
        months.add("Março");
        months.add("Abril");
        months.add("Maio");
        months.add("Junho");
        months.add("Julho");
        months.add("Agosto");
        months.add("Setembro");
        months.add("Outubro");
        months.add("Novembro");
        months.add("Dezembro");
        return months;
    }

    private static DataSet populateEntrances(List<Entrance> entrances, int month, int year, List<Long> owners) {
        DataSet dataSet = new DataSet();
        ArrayList<BigDecimal> data = new ArrayList<>();

        BigDecimal month1 = new BigDecimal(BigInteger.ZERO);
        BigDecimal month2 = new BigDecimal(BigInteger.ZERO);
        BigDecimal month3 = new BigDecimal(BigInteger.ZERO);
        BigDecimal month4 = new BigDecimal(BigInteger.ZERO);
        BigDecimal month5 = new BigDecimal(BigInteger.ZERO);
        BigDecimal month6 = new BigDecimal(BigInteger.ZERO);
        BigDecimal month7 = new BigDecimal(BigInteger.ZERO);
        BigDecimal month8 = new BigDecimal(BigInteger.ZERO);
        BigDecimal month9 = new BigDecimal(BigInteger.ZERO);
        BigDecimal month10 = new BigDecimal(BigInteger.ZERO);
        BigDecimal month11 = new BigDecimal(BigInteger.ZERO);
        BigDecimal month12 = new BigDecimal(BigInteger.ZERO);

        for (Entrance entrance : entrances) {
            LocalDate initialDate = entrance.getInitialDate().toLocalDateTime().toLocalDate();
            int initialMonth = initialDate.getMonthValue();
            int initialYear = initialDate.getYear();
            if (entrance.getFrequency().equalsIgnoreCase("mensal") && owners.contains(entrance.getOwnerId())) {
                if (initialMonth <= 1 || initialYear < year) {
                    month1 = month1.add(entrance.getSalary());
                }
                if (initialMonth <= 2 || initialYear < year) {
                    month2 = month2.add(entrance.getSalary());
                }
                if (initialMonth <= 3 || initialYear < year) {
                    month3 = month3.add(entrance.getSalary());
                }
                if (initialMonth <= 4 || initialYear < year) {
                    month4 = month4.add(entrance.getSalary());
                }
                if (initialMonth <= 5 || initialYear < year) {
                    month5 = month5.add(entrance.getSalary());
                }
                if (initialMonth <= 6 || initialYear < year) {
                    month6 = month6.add(entrance.getSalary());
                }
                if (initialMonth <= 7 || initialYear < year) {
                    month7 = month7.add(entrance.getSalary());
                }
                if (initialMonth <= 8 || initialYear < year) {
                    month8 = month8.add(entrance.getSalary());
                }
                if (initialMonth <= 9 || initialYear < year) {
                    month9 = month9.add(entrance.getSalary());
                }
                if (initialMonth <= 10 || initialYear < year) {
                    month10 = month10.add(entrance.getSalary());
                }
                if (initialMonth <= 11 || initialYear < year) {
                    month11 = month11.add(entrance.getSalary());
                }
                if (initialMonth <= 12 || initialYear < year) {
                    month12 = month12.add(entrance.getSalary());
                }

            } else {
                if (entrance.getFrequency().equalsIgnoreCase("anual") && owners.contains(entrance.getOwnerId()) && initialMonth <= month && year == initialYear) {
                    if (entrance.getMonthReceive() == 1) {
                        month1 = month1.add(entrance.getSalary());
                    } else if (entrance.getMonthReceive() == 2) {
                        month2 = month2.add(entrance.getSalary());
                    } else if (entrance.getMonthReceive() == 3) {
                        month3 = month3.add(entrance.getSalary());
                    } else if (entrance.getMonthReceive() == 4) {
                        month4 = month4.add(entrance.getSalary());
                    } else if (entrance.getMonthReceive() == 5) {
                        month5 = month5.add(entrance.getSalary());
                    } else if (entrance.getMonthReceive() == 6) {
                        month6 = month6.add(entrance.getSalary());
                    } else if (entrance.getMonthReceive() == 7) {
                        month7 = month7.add(entrance.getSalary());
                    } else if (entrance.getMonthReceive() == 8) {
                        month8 = month8.add(entrance.getSalary());
                    } else if (entrance.getMonthReceive() == 9) {
                        month9 = month9.add(entrance.getSalary());
                    } else if (entrance.getMonthReceive() == 10) {
                        month10 = month10.add(entrance.getSalary());
                    } else if (entrance.getMonthReceive() == 11) {
                        month11 = month11.add(entrance.getSalary());
                    } else if (entrance.getMonthReceive() == 12) {
                        month12 = month12.add(entrance.getSalary());
                    }

                } else if (entrance.getFrequency().equalsIgnoreCase("única") && owners.contains(entrance.getOwnerId()) && initialMonth <= month) {
                    if (initialMonth == 1 && initialYear == year) {
                        month1 = month1.add(entrance.getSalary());
                    } else if (initialMonth == 2 && initialYear == year) {
                        month2 = month2.add(entrance.getSalary());
                    } else if (initialMonth == 3 && initialYear == year) {
                        month3 = month3.add(entrance.getSalary());
                    } else if (initialMonth == 4 && initialYear == year) {
                        month4 = month4.add(entrance.getSalary());
                    } else if (initialMonth == 5 && initialYear == year) {
                        month5 = month5.add(entrance.getSalary());
                    } else if (initialMonth == 6 && initialYear == year) {
                        month6 = month6.add(entrance.getSalary());
                    } else if (initialMonth == 7 && initialYear == year) {
                        month7 = month7.add(entrance.getSalary());
                    } else if (initialMonth == 8 && initialYear == year) {
                        month8 = month8.add(entrance.getSalary());
                    } else if (initialMonth == 9 && initialYear == year) {
                        month9 = month9.add(entrance.getSalary());
                    } else if (initialMonth == 10 && initialYear == year) {
                        month10 = month10.add(entrance.getSalary());
                    } else if (initialMonth == 11 && initialYear == year) {
                        month11 = month11.add(entrance.getSalary());
                    } else if (initialMonth == 12 && initialYear == year) {
                        month12 = month12.add(entrance.getSalary());
                    }
                }
            }
        }
        data.add(month1);
        data.add(month2);
        data.add(month3);
        data.add(month4);
        data.add(month5);
        data.add(month6);
        data.add(month7);
        data.add(month8);
        data.add(month9);
        data.add(month10);
        data.add(month11);
        data.add(month12);

        dataSet.setLabel("Receita");
        dataSet.setBackgroundColor("#01b8aa");
        dataSet.setBorderColor("#01b8aa");
        dataSet.setData(data);
        return dataSet;

    }

    private static DataSet populateExpenses(List<Expense> expenses, List<BankMovement> bankMovements, int month, int year, List<Long> owners, List<Forecast> forecasts, List<SpecificGroup> specificGroups) {
        DataSet dataSet = new DataSet();
        ArrayList<BigDecimal> data = new ArrayList<>();

        BigDecimal[] months = new BigDecimal[12];

        for (int i = 0; i < 12; i++) {
            months[i] = new BigDecimal(BigInteger.ZERO);
        }

        for (int i = 0; i < 12; i++) {
            for (Expense expense : expenses) {
                int monthValue = i + 1;
                if (owners.contains(expense.getOwnerId())) {
                    String monthValidate = (monthValue < 10) ? "0" + monthValue : "" + monthValue;
                    String period = monthValidate + "/" + year;

                    List<BankMovement> bankMovementList = bankMovements.stream()
                            .filter(bm -> Objects.equals(bm.getExpenseId(), expense.getId()) &&
                                    bm.getReferencePeriod().equalsIgnoreCase(period) &&
                                    bm.getType().equalsIgnoreCase("Saída")
                            ).collect(Collectors.toList());

                    BigDecimal value = bankMovementList.stream().map(BankMovement::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);
                    String status = GetStatusPayment.getStatus(expense, bankMovementList, i + 1, year);
                    if (!status.equalsIgnoreCase("Não Iniciada") && !status.isEmpty()) {
                        expense.setStatus(status);
                        if (status.equalsIgnoreCase("Confirmado")) {
                            months[i] = months[i].add(value);
                        } else {
                            BigDecimal valueToAdd = expense.getHasSplitExpense() ?
                                    expense.getValue().divide(new BigDecimal(expense.getQuantityPart()), MathContext.DECIMAL32) :
                                    expense.getValue();

                            months[i] = months[i].add(valueToAdd);
                        }

                    }
                }
            }
        }

        data.addAll(Arrays.asList(months));

        dataSet.setLabel("Despesa");
        dataSet.setBackgroundColor("red");
        dataSet.setBorderColor("red");
        dataSet.setData(data);
        return dataSet;
    }

    private static DataSet populateLeft(DataSet receive, DataSet expense) {
        DataSet dataSet = new DataSet();
        ArrayList<BigDecimal> data = new ArrayList<>();

        BigDecimal month1 = receive.getData().get(0).subtract(expense.getData().get(0));
        BigDecimal month2 = receive.getData().get(1).subtract(expense.getData().get(1));
        BigDecimal month3 = receive.getData().get(2).subtract(expense.getData().get(2));
        BigDecimal month4 = receive.getData().get(3).subtract(expense.getData().get(3));
        BigDecimal month5 = receive.getData().get(4).subtract(expense.getData().get(4));
        BigDecimal month6 = receive.getData().get(5).subtract(expense.getData().get(5));
        BigDecimal month7 = receive.getData().get(6).subtract(expense.getData().get(6));
        BigDecimal month8 = receive.getData().get(7).subtract(expense.getData().get(7));
        BigDecimal month9 = receive.getData().get(8).subtract(expense.getData().get(8));
        BigDecimal month10 = receive.getData().get(9).subtract(expense.getData().get(9));
        BigDecimal month11 = receive.getData().get(10).subtract(expense.getData().get(10));
        BigDecimal month12 = receive.getData().get(11).subtract(expense.getData().get(11));

        data.add(month1);
        data.add(month2);
        data.add(month3);
        data.add(month4);
        data.add(month5);
        data.add(month6);
        data.add(month7);
        data.add(month8);
        data.add(month9);
        data.add(month10);
        data.add(month11);
        data.add(month12);

        dataSet.setLabel("Sobra");
        dataSet.setBackgroundColor("#0195f5");
        dataSet.setBorderColor("#0195f5");
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
        for (int i = 0; i < dataList.size(); i++) {
            receiveTotal = receiveTotal.add(dataList.get(i));
        }
        return receiveTotal;
    }

}
